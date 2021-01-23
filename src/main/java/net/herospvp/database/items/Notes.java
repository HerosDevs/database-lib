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

    public String selectWhere(String target, Object value) {
        return selectWhere(target, target, value);
    }

    public String selectWhere(String target, String field, Object value) {
        return "SELECT " + target + " FROM " + table + " WHERE " + field + " = " + convertObj(value) + ";";
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
        stringBuilder.append(") ").append("VALUES (");

        for (int i = 0; i < values.length; i++) {
            stringBuilder.append(convertObj(values[i]));

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
            stringBuilder.append(fields[i]).append(" = ").append(convertObj(values[i]));

            if (i != fields.length - 1)
                stringBuilder.append(", ");
        }

        stringBuilder.append(" WHERE ").append(target).append(" = ").append(convertObj(targetValue)).append(";");

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

    public String insertIfNotExist(String[] fields, Object[] values, String target, Object object) {

        StringBuilder stringBuilder = new StringBuilder("INSERT INTO ");

        stringBuilder.append(table).append(" (");

        for (int i = 0; i < fields.length; i++) {
            stringBuilder.append(fields[i]);
            if (i != fields.length - 1)
                stringBuilder.append(", ");
        }

        stringBuilder.append(") SELECT * FROM (SELECT ");

        for (int i = 0; i < values.length; i++) {
            stringBuilder.append(convertObj(values[i])).append(" AS ").append(fields[i]);

            if (i != values.length - 1)
                stringBuilder.append(", ");
        }

        stringBuilder.append(") AS tmp WHERE NOT EXISTS (SELECT ").append(target).append(" FROM ")
                .append(table).append(" WHERE ").append(target).append(" = ");

        stringBuilder.append(convertObj(object)).append(") LIMIT 1;");

        return stringBuilder.toString();
    }

    private String convertObj(Object object) {
        StringBuilder stringBuilder = new StringBuilder();

        if (object instanceof String) {
            stringBuilder.append("\"").append(object).append("\"");
        } else if (object instanceof Boolean) {
            stringBuilder.append((Boolean) object ? 1 : 0);
        } else {
            stringBuilder.append(object);
        }

        return stringBuilder.toString();
    }

}
