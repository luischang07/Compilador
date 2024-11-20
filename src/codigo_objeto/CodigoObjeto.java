package codigo_objeto;

import java.util.HashMap;
import java.util.Map;

public class CodigoObjeto {

    private Map<String, String> opcodeMap;
    private Map<String, String[]> registerMap;

    public CodigoObjeto() {
        opcodeMap = new HashMap<>();
        registerMap = new HashMap<>();
        initializeMaps();
    }

    private void initializeMaps() {
        opcodeMap.put("mov_reg_reg", "1000100");
        opcodeMap.put("mov_imm_reg", "1011");
        opcodeMap.put("mov_mem_reg", "1000100");
        opcodeMap.put("mov_reg_mem", "1000100");
        opcodeMap.put("mov_imm_mem", "1100011");
        opcodeMap.put("lea", "10001101"); // 8D
        opcodeMap.put("push", "01010"); // 50-57
        opcodeMap.put("pop", "01011"); // 58-5F
        opcodeMap.put("add_reg_reg", "000000");
        opcodeMap.put("add_reg_imm", "100000"); // 03
        opcodeMap.put("sub_reg_imm", "100000"); // 2B
        opcodeMap.put("sub_reg_reg", "000101"); // 2B
        opcodeMap.put("mul", "1111011"); // F7
        opcodeMap.put("int", "11001101"); // CD
        opcodeMap.put("call", "11101000"); // E8
        opcodeMap.put("inc", "01000"); // 40-47
        opcodeMap.put("loop", "11100010"); // E2
        opcodeMap.put("ret", "11000011");

        registerMap.put("ax", new String[] { "000", "1" });
        registerMap.put("bx", new String[] { "011", "1" });
        registerMap.put("cx", new String[] { "001", "1" });
        registerMap.put("dx", new String[] { "010", "1" });
        registerMap.put("si", new String[] { "110", "1" });
        registerMap.put("di", new String[] { "111", "1" });
        registerMap.put("ds", new String[] { "011", "1" });
        registerMap.put("al", new String[] { "000", "0" });
        registerMap.put("bl", new String[] { "011", "0" });
        registerMap.put("cl", new String[] { "001", "0" });
        registerMap.put("dl", new String[] { "010", "0" });
        registerMap.put("ah", new String[] { "100", "0" });
        registerMap.put("bh", new String[] { "111", "0" });
        registerMap.put("ch", new String[] { "101", "0" });
        registerMap.put("dh", new String[] { "110", "0" });
    }

    public Map<String, String> getOpcodeMap() {
        return opcodeMap;
    }

    public Map<String, String[]> getRegisterMap() {
        return registerMap;
    }
}
