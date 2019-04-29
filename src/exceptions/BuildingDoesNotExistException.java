package exceptions;

public class BuildingDoesNotExistException extends MonopolyException {
    public BuildingDoesNotExistException(String message) {
        super( message );
    }
}
