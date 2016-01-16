package denton.formsspammer;

public class FormElement {
	public final ElementType type;
	public final String name;
	public final String value;

	public FormElement(ElementType type, String name, String value) {
		this.type = type;
		this.name = name;
		this.value = value;
	}
}
