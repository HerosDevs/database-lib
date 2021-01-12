package net.herospvp.database.items;

import lombok.Getter;

public class Notes {

    @Getter
    private final String table;

    public Notes(String table) {
        this.table = table;
    }

    public String selectAll() {
        return "SELECT * FROM " + table + ";";
    }

    public String select(String field) {
        return "SELECT " + field + " FROM " + table + ";";
    }

    public String selectWhere(String field, Object value) {
        String string;

        if (value instanceof String) {
            string = "\"" + value + "\"";
        } else if (value instanceof Boolean) {
            string = String.valueOf((Boolean) value ? 1 : 0);
        } else {
            string = String.valueOf(value);
        }

        return "SELECT " + field + " FROM " + table + " WHERE " + field + " = " + string + ";";
    }

    public String insert(String[] fields, Object[] values) {
        StringBuilder stringBuilder = new StringBuilder("INSERT INTO ");
        stringBuilder.append(table);

        stringBuilder.append(" (");
        for (int i = 0; i < fields.length; i++) {
            stringBuilder.append(fields[i]);

            if (i != fields.length - 1)
                stringBuilder.append(", ");
        }
        stringBuilder.append(") ");

        stringBuilder.append("VALUES (");
        for (int i = 0; i < values.length; i++) {

            Object var = values[i];

            if (var instanceof String) {
                stringBuilder.append("\"").append(values[i]).append("\"");
            } else if (var instanceof Boolean) {
                stringBuilder.append((Boolean) values[i] ? 1 : 0);
            } else {
                stringBuilder.append(var);
            }

            if (i != values.length - 1)
                stringBuilder.append(", ");
        }
        stringBuilder.append(");");

        return stringBuilder.toString();
    }

    public String update(String[] fields, Object[] values, String target, Object targetValue) {
        StringBuilder stringBuilder = new StringBuilder("UPDATE ");

        stringBuilder.append(table).append(" SET ");

        for (int i = 0; i < fields.length; i++) {
            stringBuilder.append(fields[i]).append(" = ");

            Object var = values[i];

            if (var instanceof String) {
                stringBuilder.append("\"").append(values[i]).append("\"");
            } else if (var instanceof Boolean) {
                stringBuilder.append((Boolean) values[i] ? 1 : 0);
            } else {
                stringBuilder.append(var);
            }

            if (i != fields.length - 1)
                stringBuilder.append(", ");
        }

        StringBuilder targetBuilder = new StringBuilder();
        if (targetValue instanceof String) {
            targetBuilder.append("\"").append(targetValue).append("\"");
        } else if (targetValue instanceof Boolean) {
            targetBuilder.append((Boolean) targetValue ? 1 : 0);
        } else {
            targetBuilder.append(targetValue);
        }

        stringBuilder.append(" WHERE ").append(target).append(" = \"").append(targetBuilder).append("\";");

        return stringBuilder.toString();
    }

    public String createTable(String[] fields) {
        StringBuilder stringBuilder = new StringBuilder("CREATE TABLE IF NOT EXISTS ");

        stringBuilder.append(table).append(" (");

        for (int i = 0; i < fields.length; i++) {
            stringBuilder.append(fields[i]);
            if (i != fields.length - 1)
                stringBuilder.append(", ");
        }

        stringBuilder.append(");");

        return stringBuilder.toString();
    }

}
