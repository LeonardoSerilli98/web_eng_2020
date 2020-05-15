package data;

/**
 *
 * @author giuse
 */
public class DataItemProxyImpl extends DataItemImpl implements Data_ItemProxy {

    private boolean dirty;

    public DataItemProxyImpl() {
        this.dirty = false;
    }

    @Override
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    @Override
    public boolean isDirty() {
        return dirty;
    }
}
