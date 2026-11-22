package org.linlinjava.litemall.db.util;

import java.util.regex.Pattern;

/**
 * 排序子句构造工具：对来自前端的排序字段与排序方向做白名单式校验，
 * 防止排序参数被拼进 order by 子句造成注入。列名仅允许字母、数字与下划线，
 * 方向仅允许 asc/desc；非法输入返回 null，调用方应回退默认排序（即不设置排序子句）。
 */
public final class OrderByUtil {

    private OrderByUtil() {
    }

    private static final Pattern COLUMN_PATTERN = Pattern.compile("[A-Za-z0-9_]{1,64}");

    /**
     * 构造经过校验的 order by 子句片段，形如 "add_time desc"。
     *
     * @param sort  排序字段名
     * @param order 排序方向，asc 或 desc（忽略大小写）
     * @return 合法子句；字段非法或为空时返回 null
     */
    public static String build(String sort, String order) {
        if (sort == null || sort.trim().isEmpty() || order == null || order.trim().isEmpty()) {
            return null;
        }
        String column = sort.trim();
        if (!COLUMN_PATTERN.matcher(column).matches()) {
            return null;
        }
        String direction = "asc".equalsIgnoreCase(order.trim()) ? "asc" : "desc";
        return column + " " + direction;
    }
}
