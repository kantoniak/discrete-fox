package com.kantoniak.discrete_fox.communication;

public enum QuestionCategory {
    GENERAL, ECONOMY, POPULATION, INDUSTRY, AGRICULTURE, TRADE, TRANSPORT, ENVIRONMENT, SCIENCE;

    private static final int[] MINCOLOR = {0xFF7986CB, 0xFFF06292, 0xFFFFD54F, 0xFF4DD0E1, 0xFFAED581, 0xFFE57373, 0xFFDCE775, 0xFF80CBC4, 0xFFFFB74D};
    private static final int[] MAXCOLOR = {0xFF283593, 0xFFAD1457, 0xFFFF8F00, 0xFF00838F, 0xFF558B2F, 0xFFC62828, 0xFF9E9D24, 0xFF00695C, 0xFFEF6C00};

    public int getMinColor() {
        return MINCOLOR[this.ordinal()];
    }

    public int getMaxColor() {
        return MAXCOLOR[this.ordinal()];
    }
}
