
package com.example.datafilestrategieexporter.domain.entities;

import com.example.datafilestrategieexporter.domain.annotations.ExcelColumn;
import com.example.datafilestrategieexporter.domain.annotations.IgnoreIfEmpty;
import com.example.datafilestrategieexporter.domain.annotations.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EntityTwoData {

    @ExcelColumn(name = "Hashcode")
    @Order(1)
    private int hash;

    @ExcelColumn(name = "Lorem")
    @Order(2)
    private String lorem;

    @ExcelColumn(name = "Month")
    @Order(3)
    private Integer month;

    @ExcelColumn(name = "Latitude")
    @Order(4)
    @IgnoreIfEmpty
    private String latitude;

}
    