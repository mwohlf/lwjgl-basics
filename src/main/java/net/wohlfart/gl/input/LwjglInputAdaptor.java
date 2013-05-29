package net.wohlfart.gl.input;


/**
 * <p>LwjglInputAdaptor class.</p>
 *
 *
 *
 */
public class LwjglInputAdaptor  {

    // event sink, parent component we need to post the events to
    private  InputDispatcher inputDispatcher;

    // core of the adaptor, mapping platform keys to command events, the events are reused
    // @formatter:off


    /*

    public static final int MOUSE_KEY0 = 0;
    public static final int MOUSE_KEY1 = 1;
    public static final int MOUSE_KEY2 = 2;
    @SuppressWarnings("serial")
    private final HashMap<Integer, PositionEvent> mouseMap = new HashMap<Integer, PositionEvent>() {{
        put(MOUSE_KEY0, new CommandEvent.LeftClick());
        put(MOUSE_KEY1, new CommandEvent.RightClick());
        put(MOUSE_KEY2, new CommandEvent.MiddleClick());
    }};
    private final CommandEvent.MoveForward wheelForward =  new CommandEvent.MoveForward();
    private final CommandEvent.MoveBackward wheelBackward =  new CommandEvent.MoveBackward();


    private final PositionPointer positionPointer = new PositionPointer();
    // @formatter:on


    public LwjglInputAdaptor(InputDispatcher inputDispatcher) {
        this.inputDispatcher = inputDispatcher;
    }


    private final PositionEventDispatcher mouseDevice = new PositionEventDispatcher() {

        public void wheel(int amount) {
            if (amount > 0) {
                wheelForward.setWheelAmount(amount);
                inputDispatcher.post(wheelForward);
            } else {
                wheelBackward.setWheelAmount(-amount);
                inputDispatcher.post(wheelBackward);
            }
        }

        public void up(int key, int x, int y, float time) {
        }

        public void down(int key, int x, int y, float time) {
            PositionEvent event = mouseMap.get(key);
            event.setPosition(x, y);
            inputDispatcher.post(event);
        }

        public void position(int posX, int posY) {
            positionPointer.setPosition(posX, posY);
            inputDispatcher.post(positionPointer);
        }

        public void move(int key, int deltaX, int deltaY) {
            switch (key) {
            case MOUSE_KEY0:
                break;
            case MOUSE_KEY1:
                break;
            case MOUSE_KEY2:
                break;
            }
        }

    };

*/

    /*
    @Override
    public KeyEventDispatcher getKeyboardDevice() {
        return keyboardDevice;
    }

    @Override
    public PositionEventDispatcher getMouseDevice() {
        return mouseDevice;
    }

    @Override
    public void destroy() {
        inputDispatcher.destroy();
    }
    */
}
