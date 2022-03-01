package com.PDFGenerator.PDFGenerator.utils;

public interface CONST {
    String PATH_TO_FILE = "src/main/resources/orderLines.csv";
    String FILE_NOT_READ = "Could not read file";
    String TABLE_STYLE = """
   <style type="text/css">
   TABLE {
    background: black; /* Цвет фона таблицы */
    color: black; /* Цвет текста */
   }
   TD, TH {
    background: ghostwhite; /* Цвет фона ячеек */
    padding: 5px; /* Поля вокруг текста */
   }
  </style>
<table>
                    """;
}
