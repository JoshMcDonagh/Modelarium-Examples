package sirbasic.environment.attributes.geography;

import modelarium.attributes.Property;

public class WidthProperty extends Property<Integer> {
    private Integer width = null;

    public WidthProperty() {
        super("width", false, Integer.class);
    }

    @Override
    public void set(Integer width) {
        if (this.width != null)
            return;
        this.width = width;
    }

    @Override
    public Integer get() {
        return width;
    }

    @Override
    public void run() {}
}
