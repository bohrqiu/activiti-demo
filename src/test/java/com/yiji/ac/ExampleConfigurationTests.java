package com.yiji.ac;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class ExampleConfigurationTests {
	
	private static Logger logger = LoggerFactory.getLogger(ExampleConfigurationTests.class);
	
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private ProcessEngine processEngine;
	
	@Autowired
	RepositoryService repositoryService;
	
	@Test
	public void deployProcessDefinition1() {
		repositoryService.deleteDeployment("101");
//		repositoryService.createDeployment()
//			.addClasspathResource("org/activiti/test/VacationRequest.bpmn20.xml").deploy();
//		
//		logger.info("Number of process definitions: "
//					+ repositoryService.createProcessDefinitionQuery().count());
	}
	
	
	/**
	 * 部署流程定义
	 */
	@Test
//	@Ignore
	public void deployProcessDefinition() {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		RepositoryService repositoryService = processEngine.getRepositoryService();
		repositoryService.createDeployment()
			.addClasspathResource("org/activiti/test/VacationRequest.bpmn20.xml").deploy();
		
		logger.info("Number of process definitions: "
					+ repositoryService.createProcessDefinitionQuery().count());
	}
	
	/**
	 * 请假流程
	 */
	@Test
	public void startAndCompleteProcessInstance() {
		//1.员工启动流程实例
		String employeeName = "Kermit";
		String management = "management";
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("employeeName", employeeName);
		variables.put("numberOfDays", new Integer(4));
		variables.put("vacationMotivation", "I'm really tired!");
		
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(
			"vacationRequest", variables);
		logger.info("{}发起请假流程:{}", employeeName, variables);
		logger.info("当前流程实例数: " + runtimeService.createProcessInstanceQuery().count());
		
		//2.管理员拒绝员工请假
		List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup(management).list();
		for (Task task : tasks) {
			logger.info("{} 待处理任务: {}: ", management, task.getName());
			Map<String, Object> taskVariables = new HashMap<String, Object>();
			taskVariables.put("vacationApproved", "false");
			taskVariables.put("managerMotivation", "We have a tight deadline!");
			taskService.complete(task.getId(), taskVariables);
			logger.info("{} 处理任务: {}", management, taskVariables);
		}
		//3.员工重新调整请假时间
		tasks = taskService.createTaskQuery().taskAssignee(employeeName).list();
		for (Task task : tasks) {
			logger.info("{} 待处理任务: {}", employeeName, task.getName());
			Map<String, Object> taskVariables = new HashMap<String, Object>();
			taskVariables.put("numberOfDays", 2);
			taskVariables.put("startDate", new Date());
			taskVariables.put("vacationMotivation", "真的遭不住老啊");
			taskVariables.put("resendRequest", "true");
			taskService.complete(task.getId(), taskVariables);
			logger.info("{} 处理任务: {}", employeeName, taskVariables);
		}
		//4.管理员批准员工请假
		tasks = taskService.createTaskQuery().taskCandidateGroup(management).list();
		for (Task task : tasks) {
			logger.info("{} 待处理任务: {}: ", management, task.getName());
			Map<String, Object> taskVariables = new HashMap<String, Object>();
			taskVariables.put("vacationApproved", "true");
			taskVariables.put("managerMotivation", "免得你挂了,还是给你批准了!");
			taskService.complete(task.getId(), taskVariables);
			logger.info("{} 处理任务: {}", management, taskVariables);
		}
		//5.查看当前流程实例
		logger.info("当前流程实例数: " + runtimeService.createProcessInstanceQuery().count());
	}
	
}
