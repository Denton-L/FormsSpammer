package denton.formsspammer;

public class FormElement {
	public final SubmissionElement submissionElement;
	public final String name;
	
	public FormElement(SubmissionElement submissionElement, String name) {
		this.submissionElement = submissionElement;
		this.name = name;
	}
	
	public String toString() {
		return submissionElement.toString() +", " +name;
	}
}
