module org.sistemaestoque {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.sistemaestoque.view to javafx.fxml;
    exports org.sistemaestoque.view;
    exports org.sistemaestoque.model;
    exports org.sistemaestoque.da0;
}