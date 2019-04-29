package exceptions;

public class MultiplePathException extends MonopolyException{

    public MultiplePathException(String message) {
        super( message );
    }

    public void choosePath(){
        //TODO: popup that lets you choose path
    }
}
