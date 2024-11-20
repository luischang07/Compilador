package codigo_objeto;

import java.util.Map;
import java.util.HashMap;

//Call y loop pendientes xd
public class CodeSegmentTranslator {
    private int currentOffset = 0;
    private static final String CODE_SEGMENT = "0001"; // Segment code
    private final Map<String, String> opcodeMap;
    private final Map<String, String[]> registerMap;
    private Map<String, OffsetRange> variableOffsets = new HashMap<>();

    public CodeSegmentTranslator(Map<String, String> opcodeMap, Map<String, String[]> registerMap,
            Map<String, OffsetRange> variableOffsets) {
        this.opcodeMap = opcodeMap;
        this.variableOffsets = variableOffsets;
        this.registerMap = registerMap;
    }

    public String translateInstruction(String line) {

        if (line.startsWith(";") || line.isEmpty())
            return "";

        String[] parts = line.trim().split("\\s+");
        if (parts.length == 0)
            return "";

        String offsetHex = String.format("%04X", currentOffset);
        StringBuilder binaryCode = new StringBuilder();
        binaryCode.append(String.format("%s : %s  ", CODE_SEGMENT, offsetHex));

        switch (parts[0].toLowerCase()) {
            case "mov":
                translateMov(parts, binaryCode);
                break;
            case "lea":
                translateLea(parts, binaryCode);
                break;
            case "push":
                translatePush(parts, binaryCode);
                break;
            case "pop":
                translateIncPop(parts, binaryCode);
                break;
            case "add":
                translateAdd(parts, binaryCode);
                break;
            case "sub":
                translateSub(parts, binaryCode);
                break;
            case "mul":
                translateMul(parts, binaryCode);
                break;
            case "inc":
                translateIncPop(parts, binaryCode);
                break;
            case "int":
                translateInt(parts, binaryCode);
                break;
            case "ret":
                translateRet(parts, binaryCode);
                break;
            default:
                return "Unknown instruction: " + parts[0] + "\n";
        }

        binaryCode.append("\n");
        return binaryCode.toString();
    }

    private void translateMov(String[] parts, StringBuilder binaryCode) {
        if (parts.length != 3)
            return;

        String dest = cleanRegister(parts[1]);
        String src = parts[2];

        if (src.startsWith("[")) {
            // mov reg, [mem]
            String opcode = opcodeMap.get("mov_mem_reg");
            binaryCode.append(opcode).append(registerMap.get(dest)[1] + " ");

            binaryCode.append("00"); // Example for mod
            String regBits = registerMap.getOrDefault(dest, new String[] { "000" })[0];

            String reg2 = src.replace("[", "").replace("]", "");
            String reg2Bits = registerMap.getOrDefault(reg2, new String[] { "000" })[0];

            binaryCode.append(regBits).append(reg2Bits);
            currentOffset += 2;
        } else if (isImmediate(src)) {
            // mov reg, imm
            String opcode = opcodeMap.get("mov_imm_reg");
            binaryCode.append(opcode).append(registerMap.get(dest)[1]);
            String regBits = registerMap.getOrDefault(dest, new String[] { "000" })[0];
            binaryCode.append(regBits).append(" ");
            binaryCode.append(convertImmediateToBinary(src));
            currentOffset += 3;
        } else {
            // mov reg, reg
            String opcode = opcodeMap.get("mov_reg_reg");
            dest = dest.replace("[", "").replace("]", "");
            if (!registerMap.containsKey(dest)) {
                // mov [mem], reg
                opcode = opcodeMap.get("mov_reg_mem");
                binaryCode.append(opcode).append(registerMap.get(src)[1] + " ");
                binaryCode.append("00"); // Example for mod
                String regBits = registerMap.getOrDefault(src, new String[] { "000" })[0];
                binaryCode.append(regBits).append("101");
                binaryCode.append(" ").append("?????");
                currentOffset += 2;
            } else {
                binaryCode.append(opcode).append(registerMap.get(dest)[1] + " ");
            }
            String srcBits = registerMap.getOrDefault(src, new String[] { "000" })[0];
            String destBits = registerMap.getOrDefault(dest, new String[] { "000" })[0];
            binaryCode.append("11").append(srcBits).append(destBits);
            currentOffset += 2;
        }
    }

    private void translateLea(String[] parts, StringBuilder binaryCode) {
        if (parts.length != 3)
            return;

        String opcode = opcodeMap.get(parts[0]);
        binaryCode.append(opcode).append(" ");

        String dest = cleanRegister(parts[1]);
        String variable = parts[2];

        String destBits = registerMap.getOrDefault(dest, new String[] { "000" })[0];
        int offset = variableOffsets.get(variable).getStartOffset();

        binaryCode.append("00").append(destBits).append("101");

        String offsetBinary = String.format("%016d", Integer.parseInt(Integer.toBinaryString(offset)));
        binaryCode.append(" ").append(offsetBinary);

        currentOffset += 4;
    }

    private void translateInt(String[] parts, StringBuilder binaryCode) {
        if (parts.length != 2)
            return;

        String opCode = opcodeMap.get(parts[0]);

        binaryCode.append(opCode + " "); // CD
        String value = parts[1].replace("0x", "").replace("h", "");
        binaryCode.append(convertByteToBinary(Integer.parseInt(value, 16)));
        currentOffset += 2;
    }

    private String cleanRegister(String register) {
        return register.replace(",", "").toLowerCase();
    }

    private boolean isImmediate(String operand) {
        return operand.matches("\\s*[0-9a-fA-F]+h");
    }

    private String convertImmediateToBinary(String immediate) {
        int value;
        if (immediate.endsWith("h")) {
            value = Integer.parseInt(immediate.replace("h", ""), 16);
        } else {
            value = Integer.parseInt(immediate);
        }
        return convertByteToBinary(value);
    }

    private String convertByteToBinary(int value) {
        return String.format("%8s", Integer.toBinaryString(value & 0xFF)).replace(' ', '0');
    }

    private void translatePush(String[] parts, StringBuilder binaryCode) {
        if (parts.length != 2)
            return;

        String reg = cleanRegister(parts[1]);
        if (isImmediate(reg)) {
            // push immediate
            binaryCode.append(opcodeMap.get(parts[0]) + " ");
            binaryCode.append(convertByteToBinary(Integer.parseInt(reg)));
            currentOffset += 2;
        } else {
            // push register
            binaryCode.append(opcodeMap.get(parts[0]) + " ");
            String regBits = registerMap.getOrDefault(reg, new String[] { "000" })[0];
            binaryCode.append(regBits);
            currentOffset += 1;
        }
    }

    private void translateIncPop(String[] parts, StringBuilder binaryCode) {
        if (parts.length != 2)
            return;

        String reg = cleanRegister(parts[1]);
        binaryCode.append(opcodeMap.get(parts[0]));
        String regBits = registerMap.getOrDefault(reg, new String[] { "000" })[0];
        binaryCode.append(regBits);
        currentOffset += 1;
    }

    private void translateAdd(String[] parts, StringBuilder binaryCode) {
        if (parts.length != 3)
            return;

        String dest = cleanRegister(parts[1]);
        String src = parts[2];
        String destReg = registerMap.get(dest)[1];

        if (isImmediate(src)) {
            // add reg, immediate
            binaryCode.append(opcodeMap.get("add_reg_imm") + "0").append(destReg + " ");

            String regBits = registerMap.getOrDefault(dest, new String[] { "000" })[0];

            String oo;
            if (destReg.equals("1"))
                oo = "10";
            else
                oo = "01";
            binaryCode.append(oo + "000" + regBits).append(" ");
            binaryCode.append(convertImmediateToBinary(src));
            currentOffset += 3;
        } else {
            // add reg, reg
            binaryCode.append(opcodeMap.get("add_reg_reg") + "0").append(destReg + " ");
            String destBits = registerMap.getOrDefault(dest, new String[] { "000" })[0];
            String srcBits = registerMap.getOrDefault(src, new String[] { "000" })[0];
            binaryCode.append("11").append(srcBits).append(destBits);
            currentOffset += 2;
        }
    }

    private void translateSub(String[] parts, StringBuilder binaryCode) {
        if (parts.length != 3)
            return;

        String dest = cleanRegister(parts[1]);
        String src = parts[2];
        String destReg = registerMap.get(dest)[1];

        if (isImmediate(src)) {
            // sub reg, immediate
            binaryCode.append(opcodeMap.get("sub_reg_imm") + "0").append(destReg + " ");
            String oo;
            if (destReg.equals("1"))
                oo = "10";
            else
                oo = "01";
            String regBits = registerMap.getOrDefault(dest, new String[] { "000" })[0];
            binaryCode.append(oo).append("101").append(regBits + " ");
            binaryCode.append(convertImmediateToBinary(src));
            currentOffset += 3;
        } else {
            // sub reg, reg
            binaryCode.append(opcodeMap.get("sub_reg_reg") + "0").append(destReg + " ");

            String destBits = registerMap.getOrDefault(dest, new String[] { "000" })[0];
            String srcBits = registerMap.getOrDefault(src, new String[] { "000" })[0];
            binaryCode.append("11").append(srcBits).append(destBits);
            currentOffset += 2;
        }
    }

    private void translateMul(String[] parts, StringBuilder binaryCode) {
        if (parts.length != 2)
            return;

        String src = cleanRegister(parts[1]);
        String oo;
        if (registerMap.get(src)[1].equals("1"))
            oo = "10";
        else
            oo = "01";
        String destReg = registerMap.get(src)[1];
        binaryCode.append(opcodeMap.get(parts[0])).append(destReg + " ");
        String srcBits = registerMap.getOrDefault(src, new String[] { "000" })[0];
        binaryCode.append(oo).append("100").append(srcBits);
        currentOffset += 2;
    }

    private void translateRet(String[] parts, StringBuilder binaryCode) {
        binaryCode.append(opcodeMap.get(parts[0]));
        currentOffset += 1;
    }

    public void resetOffset() {
        currentOffset = 0;
    }

    public int getCurrentOffset() {
        return currentOffset;
    }
}