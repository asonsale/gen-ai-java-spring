package com.geni.java.spring.chat.openai.dto.response;

import java.util.List;

public class SummarizationResponse {

    private List<String> actionItems;
    private List<String> decisions;
    private String errorMessage;

    public List<String> getActionItems() {
        return actionItems;
    }

    public void setActionItems(List<String> actionItems) {
        this.actionItems = actionItems;
    }

    public List<String> getDecisions() {
        return decisions;
    }

    public void setDecisions(List<String> decisions) {
        this.decisions = decisions;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}