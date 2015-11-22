package denton.formsspammer;

public class FormElement {
	public final ElementType type;
	public final String name;
	
	public FormElement(ElementType type, String name) {
		this.type = type;
		this.name = name;
	}
}
