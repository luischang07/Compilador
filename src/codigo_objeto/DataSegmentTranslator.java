package codigo_objeto;

import exeptions.Exceptions;
import java.util.HashMap;
import java.util.Map;

public class DataSegmentTranslator {
    private int currentOffset = 0;
    private int dataSegment = 0;
    private static final String FORMAT_STRING = "%s : %s \t %s";
    private Map<String, OffsetRange> variableOffsets = new HashMap<>();
    String label;

    public String translateDataDeclaration(String line) {
        String[] parts = line.trim().split("\\s+");

        if (parts.length < 3)
            return ""; // Línea inválida

        label = parts[0];
        String directive = parts[1].toLowerCase();
        String value = extractValue(parts);

        switch (directive) {
            case "db":
                return translateDb(value) + "\t\t;" + value + "\n";
            case "dw":
                return translateDw(value) + "\t;" + value + "\n";
            default:
                return "";
        }
    }

    private String translateDb(String value) {
        StringBuilder output = new StringBuilder();

        value = value.replaceAll("['\"'']", "");

        if (value.matches("\\d+ dup\\(\\$\\)")) {
            int count = Integer.parseInt(value.split(" ")[0]);
            for (int i = 0; i < count; i++) {
                String offsetHex = String.format("%04X", currentOffset);
                String segment = String.format("%04X", dataSegment);
                String binaryValue = "00100100"; // Placeholder for the '?' value
                output.append(String.format(FORMAT_STRING, segment, offsetHex, binaryValue));
                currentOffset++;
            }
            return output.toString();
        }

        if (value.endsWith("$")) {
            value = value.substring(0, value.length() - 1);
            int inicial = currentOffset;
            for (char c : value.toCharArray()) {
                String offsetHex = String.format("%04X", currentOffset);
                String segment = String.format("%04X", dataSegment);
                String binaryValue = String.format("%8s",
                        Integer.toBinaryString(c & 0xFF))
                        .replace(' ', '0');

                output.append(String.format(FORMAT_STRING + "\n",
                        segment, offsetHex, binaryValue));

                incrementOffset(1);
            }

            addVariableOffset(label, inicial, currentOffset - 1);

            String offsetHex = String.format("%04X", currentOffset);
            String segment = String.format("%04X", dataSegment);
            output.append(String.format(FORMAT_STRING,
                    segment, offsetHex, "00100100"));
            incrementOffset(1);
        } else {
            try {
                int numValue = Integer.parseInt(value);
                String offsetHex = String.format("%04X", currentOffset);
                String segment = String.format("%04X", dataSegment);
                String binaryValue = String.format("%8s",
                        Integer.toBinaryString(numValue & 0xFF))
                        .replace(' ', '0');

                output.append(String.format(FORMAT_STRING,
                        segment, offsetHex, binaryValue));

                addVariableOffset(label, currentOffset, currentOffset + 1);

                incrementOffset(1);
            } catch (NumberFormatException e) {
                throw new Exceptions("Invalid value for DB directive: " + value);
            }
        }

        return output.toString();
    }

    private String translateDw(String value) {
        StringBuilder output = new StringBuilder();
        try {
            if (value.equals("?")) {
                String offsetHex = String.format("%04X", currentOffset);
                String segment = String.format("%04X", dataSegment);
                String binaryValue = "????????????????"; // Placeholder for the '?' value
                output.append(String.format("%s:%s \t %s", segment, offsetHex, binaryValue));
                currentOffset += 2; // DW is 16 bits (2 bytes)
                return output.toString();
            }
            int numValue = Integer.parseInt(value);
            String offsetHex = String.format("%04X", currentOffset);
            String segment = String.format("%04X", dataSegment);

            // Handle the "?" case

            // DW (16 bits)
            String binaryValue = String.format("%16s",
                    Integer.toBinaryString(numValue & 0xFFFF))
                    .replace(' ', '0');

            // Concatenar los bytes alto y bajo
            String highByte = binaryValue.substring(0, 8);
            String lowByte = binaryValue.substring(8);
            String combinedBytes = highByte + " " + lowByte;

            output.append(String.format(FORMAT_STRING,
                    segment, offsetHex, combinedBytes));

            addVariableOffset(label, currentOffset, currentOffset + 2);

            incrementOffset(2);

        } catch (NumberFormatException e) {
            throw new Exceptions("Invalid value for DW directive: " + value);
        }

        return output.toString();
    }

    private void incrementOffset(int increment) {
        currentOffset += increment;
        if (currentOffset > 0xFFFF) {
            currentOffset = 0;
            dataSegment++;
        }
    }

    private String extractValue(String[] parts) {
        StringBuilder value = new StringBuilder();
        for (int i = 2; i < parts.length; i++) {
            value.append(parts[i]).append(" ");
        }
        return value.toString().trim();
    }

    public int getCurrentOffset() {
        return currentOffset;
    }

    public void resetOffset() {
        currentOffset = 0;
    }

    public Map<String, OffsetRange> variableOffsets() {
        return variableOffsets;
    }

    public void addVariableOffset(String label, int startOffset, int endOffset) {
        variableOffsets.put(label, new OffsetRange(startOffset, endOffset));
    }

}