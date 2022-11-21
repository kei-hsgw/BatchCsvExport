package com.example.demo.config.mybatis;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisCursorItemReader;
import org.mybatis.spring.batch.builder.MyBatisCursorItemReaderBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.config.BaseConfig;
import com.example.demo.domain.model.Employee;

@Configuration
public class MybatisCursorBatchConfig extends BaseConfig {

	/** SqlSessionFactory(MyBatisで必要) */
	@Autowired
	private SqlSessionFactory sqlSessionFactory;
	
	/** MyBatisCursorItemReader */
	@Bean
	@StepScope
	public MyBatisCursorItemReader<Employee> myBatisCursorreader() {
		
		// クエリに渡すパラメーター
		Map<String, Object> parameterValues = new HashMap<>();
		parameterValues.put("genderParam", 1);
		
		return new MyBatisCursorItemReaderBuilder<Employee>() // Builderの生成
				.sqlSessionFactory(sqlSessionFactory)
				.queryId("com.example.demo.repository.EmployeeMapper.findByGender")
				.parameterValues(parameterValues) // パラメーター
				.build(); // readerの生成
	}
	
	/** MyBatisCursorItemReaderを使用するStepの生成 */
	@Bean
	public Step exportMyBatisCursorStep() throws Exception {
		
		return this.stepBuilderFactory.get("ExportMyBatisCursorStep")
				.<Employee, Employee>chunk(10)
				.reader(myBatisCursorreader()).listener(readListener)
				.processor(this.genderConvertProcessor)
				.writer(csvWriter()).listener(writeListener)
				.build();
	}
	
	/** MyBatisCursorItemReaderを使用するJobの生成 */
	@Bean("MyBatisCursorJob")
	public Job exportMyBatisCursorJob() throws Exception {
		
		return this.jobBuilderFactory.get("ExportMybatisCursorJob")
				.incrementer(new RunIdIncrementer())
				.start(exportMyBatisCursorStep())
				.build();
	}
}
