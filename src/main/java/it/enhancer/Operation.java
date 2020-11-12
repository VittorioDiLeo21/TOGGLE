package it.enhancer;

public class Operation {
    private String name;
    private String parameter;

    public Operation(String name, String parameter){
        this.name = name;
        this.parameter = parameter;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getParameter() {
        return parameter;
    }
    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    @Override
    public String toString() {
        return "[name=" + name + ", parameter=" + parameter + "]";
    }
}
