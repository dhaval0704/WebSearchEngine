package ir.webutils;

public class PathDisallowedException extends Exception {

  public PathDisallowedException() {
    super();
  }

  public PathDisallowedException(String msg) {
    super(msg);
  }
}
