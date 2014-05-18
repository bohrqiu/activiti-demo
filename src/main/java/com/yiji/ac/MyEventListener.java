package com.yiji.ac;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyEventListener implements ActivitiEventListener {
	private static Logger logger = LoggerFactory.getLogger(MyEventListener.class);
	
	@Override
	public void onEvent(ActivitiEvent event) {
//		logger.info("监听事件:" + event.getType());
//		switch (event.getType()) {
//			case JOB_EXECUTION_SUCCESS:
//				logger.info("A job well done!");
//				break;
//			case JOB_EXECUTION_FAILURE:
//				logger.info("A job has failed...");
//				break;
//			default:
//				logger.info("Event received: " + event.getType());
//		}
	}
	
	@Override
	public boolean isFailOnException() {
		// The logic in the onEvent method of this listener is not critical,
		// exceptions
		// can be ignored if logging fails...
		return false;
	}
}