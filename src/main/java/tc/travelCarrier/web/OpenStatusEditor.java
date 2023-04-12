package tc.travelCarrier.web;

import tc.travelCarrier.domain.OpenStatus;

import java.beans.PropertyEditorSupport;

public class OpenStatusEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if ("public".equalsIgnoreCase(text)) {
            setValue(OpenStatus.ALL);
        } else if("private".equalsIgnoreCase(text)) {
            setValue(OpenStatus.ME);
        } else if("shareFriends".equalsIgnoreCase(text)){
            setValue(OpenStatus.FOLLOW);
        }else {
            throw new IllegalArgumentException("Invalid OpenStatus value");
        }
    }
    @Override
    public String getAsText() {
        return getValue().toString();
    }
}