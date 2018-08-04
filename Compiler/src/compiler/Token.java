package compiler;

public class Token {

    protected String content;
    protected Integer type;

    public Token(String content, Integer type) {
        this.content = content;
        this.type = type;
    }
}
