package crm.core.wfl.editor.bobj;



/**
 * User: JPB
 * Date: Jan 23, 2006
 * Time: 12:54:44 PM
 */
public class WflPosFunctionBObj extends WflConnectorBObj{
    public static final String MASK_TYPE_AND = "+";
    public static final String MASK_TYPE_OR = "-";

    private String maskType = null;
    private String maskValue = null;

    public WflPosFunctionBObj() {
        this(null, null);
    }
    public WflPosFunctionBObj(String source, String target) {
        this(source,  target,  null, null);
    }

    public WflPosFunctionBObj(String source, String target,
                              String maskType, String maskValue) {
        super(source, target);
        setMaskType(maskType);
        setMaskValue(maskValue);
    }

    public String getMaskType() {
        return maskType;
    }

    public void setMaskType(String maskType) {
        if (maskType == null || maskType.trim().equals(""))
            this.maskType = null;
        else
            this.maskType = maskType;
    }

    public String getMaskValue() {
        return maskValue;
    }

    public void setMaskValue(String maskValue) {
        this.maskValue = maskValue;
    }

}
