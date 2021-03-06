package main.generator;

import main.di.AutowiredService;
import main.service.MemberService;

public class MethodPrintGenerator extends PrintGenerator {
	private final MemberService memberService;

	public MethodPrintGenerator(AutowiredService service) {
		super(service);
		memberService = service.memberService;
	}

	@Override
	public void execute() {
	}

	//TODO: delete it soon.
	public void execute(final Object instance) {
		this.notifyObservers();
	}

	@Override
	public String getLog() {
		return "[Success] the methods of " + this.memberService.getInstance().getClass().getName() + " were created.\n";
	}
}
