package Database;

public enum DatabaseColumn {
    DOCUMENT ("Document"),
    WORD ("Word"),
    LINK ("Link"),
    SEED ("Seed");

    private final String tableName;

    DatabaseColumn(String name) {
        tableName = name;
    }

    @Override
    public String toString() {
        return tableName;
    }
}
