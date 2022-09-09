package spending_management_project.enums;

public enum IncomeType implements IntentStateWithCodeAndDescription {

    INCOME(0, "Income"),
    EXPENDITURE(1, "Spending"),
    UNNKOWN(-1, "unkown");

    private final int code;

    private final String desctiption;

    IncomeType(int code, String desctiption) {
        this.code = code;
        this.desctiption = desctiption;
    }

    public boolean isIncome() {
        return this.code == INCOME.code();
    }

    public boolean isExpend() {
        return this.code == EXPENDITURE.code();
    }


    public static IncomeType parse(int code) {
        IncomeType[] incomeTypes = IncomeType.values();
        for (IncomeType type : incomeTypes) {
            if (type.code() == code) {
                return type;
            }
        }
        return UNNKOWN;
    }

    @Override
    public int code() {
        return this.code;
    }

    @Override
    public String description() {
        return this.desctiption;
    }

}
