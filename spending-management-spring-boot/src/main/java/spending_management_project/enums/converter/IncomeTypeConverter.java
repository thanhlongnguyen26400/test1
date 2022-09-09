package spending_management_project.enums.converter;

import spending_management_project.enums.IncomeType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class IncomeTypeConverter implements AttributeConverter<IncomeType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(IncomeType incomeType) {
        if (incomeType == null) return IncomeType.UNNKOWN.code();
        return incomeType.code();
    }

    @Override
    public IncomeType convertToEntityAttribute(Integer i) {
        if (i == null) return IncomeType.UNNKOWN;
        return IncomeType.parse(i);
    }
}
