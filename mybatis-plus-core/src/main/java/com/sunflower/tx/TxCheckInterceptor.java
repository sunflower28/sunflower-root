package com.sunflower.tx;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.ibatis.mapping.SqlCommandType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * @author sunflower
 */
@Slf4j
public class TxCheckInterceptor implements MethodInterceptor {

	private TransactionInterceptor transactionInterceptor;

	public TxCheckInterceptor() {
	}

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Set<SqlCommandType> set = TxServiceHelper.get();
		TxServiceHelper.removeAll();

		Object object;
		try {
			Object proceed = invocation.proceed();
			Method method = invocation.getMethod();
			if (!method.isAnnotationPresent(Transactional.class)) {
				TransactionAttributeSource transactionAttributeSource = this.transactionInterceptor
						.getTransactionAttributeSource();
				if (null == transactionAttributeSource) {
					log.error("缺少声明式事务配置,无法进行事务检查");
				}
				else {
					TransactionAttribute attr = transactionAttributeSource
							.getTransactionAttribute(method, method.getDeclaringClass());
					if (null == attr) {
						log.error("缺少声明式事务配置,无法进行事务检查");
					}
					else {
						Set<SqlCommandType> list = TxServiceHelper.get();

						boolean flag = attr.isReadOnly()
								&& (list.contains(SqlCommandType.DELETE)
										|| list.contains(SqlCommandType.INSERT)
										|| list.contains(SqlCommandType.UPDATE));

						if (flag) {
							log.error("您的方法标志为只读,但执行了增删改操作,请修改方法的名称定义:{}.{}",
									method.getDeclaringClass(), method.getName());
						}
					}
				}
			}

			object = proceed;
		}
		finally {
			TxServiceHelper.removeAll();
			if (set != null) {
				TxServiceHelper.addAll(set);
			}

		}

		return object;
	}

	public void setTransactionInterceptor(TransactionInterceptor transactionInterceptor) {
		this.transactionInterceptor = transactionInterceptor;
	}

}
