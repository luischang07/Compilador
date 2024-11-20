package codigo_objeto;

public class OffsetRange {
    private int startOffset;
    private int endOffset;

    public OffsetRange(int startOffset, int endOffset) {
        this.startOffset = startOffset;
        this.endOffset = endOffset;
    }

    public int getStartOffset() {
        return startOffset;
    }

    public int getEndOffset() {
        return endOffset;
    }

    public void setEndOffset(int endOffset) {
        this.endOffset = endOffset;
    }

    @Override
    public String toString() {
        return "OffsetRange{" +
                "startOffset=" + startOffset +
                ", endOffset=" + endOffset +
                '}';
    }
}