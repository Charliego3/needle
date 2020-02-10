package io.github.kits.agent;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.VirtualMachine;
import io.github.kits.Envs;
import io.github.kits.Files;
import io.github.kits.Lambdas;
import io.github.kits.log.Logger;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.util.List;

/**
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public class DynamicAgent {

	private static Instrumentation instrumentation;

	public DynamicAgent() {
	}

	public static Instrumentation getInstrumentation() {
		return instrumentation;
	}

	/**
	 * 代理实现
	 *
	 * @param agentArgs 参数
	 * @param inst
	 */
	public static void agentmain(String agentArgs, Instrumentation inst) {
		synchronized (DynamicAgent.class) {
			if (instrumentation == null) {
				instrumentation = inst;
			}
		}
		inst.addTransformer(new AgentHandler());
	}

	/**
	 * 启动代理
	 *
	 * @param excludeAgentJar 需要排除的代理jar
	 */
	public static void start(String... excludeAgentJar) {
		long         currentPID = Envs.getCurrentPID();
		List<String> agentJars  = Files.findAgentJar(excludeAgentJar);
		try {
			VirtualMachine vm = VirtualMachine.attach(String.valueOf(currentPID));
			agentJars.parallelStream()
					 .forEach(Lambdas.rethrowConsumer(vm::loadAgent));
			vm.detach();
		} catch (IOException | AgentInitializationException | AgentLoadException e) {
			Logger.error("VirtualMachine loadAgent error", e);
		} catch (Exception e) {
			Logger.errorf("VirtualMachine attach error, the current PID is [{}]", e, currentPID);
		}
	}

}
