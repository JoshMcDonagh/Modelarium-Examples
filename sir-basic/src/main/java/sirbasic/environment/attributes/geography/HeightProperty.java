package sirbasic.environment.attributes.geography;

import modelarium.attributes.Property;

public class HeightProperty extends Property<Integer> {
    private Integer height = null;

    public HeightProperty() {
        super("height", false, Integer.class);
    }

    @Override
    public void set(Integer height) {
        if (this.height != null)
            return;
        this.height = height;
    }

    @Override
    public Integer get() {
        return height;
    }

    @Override
    public void run() {}
}
