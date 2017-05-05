package core.exceptions;

public class SurfaceAlreadyPresent extends LifeRuntimeException {
    public SurfaceAlreadyPresent()              { super();      }
    public SurfaceAlreadyPresent(String msg)    { super(msg);   }
}
