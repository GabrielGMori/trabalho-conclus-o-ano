package com.tca.util;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class JavaFXElementBuilder {
    public static Text text(String text, int fontSize, FontWeight fontWeight, String colorHex) {
        Text textElement = new Text(text);
        textElement.setFont(Font.font("Yu Gothic", fontWeight, fontSize));
        textElement.setFill(Color.web(colorHex));
        return textElement;
    }
}
