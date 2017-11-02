package com.rudra.aks.batch.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({DBConfig.class})
@EnableBatchProcessing
public class BatchConfig {

	@Autowired
	DataSource	dataSource;
    
    @Autowired
    private JobBuilderFactory jobs;
 
    @Autowired
    private StepBuilderFactory steps;
 
   /**
    * Simple step created with tasklet having dummy implementation
    * Add logic to perform task accordingly, may not be reading, processing or writing
    * as chunk Processing.
    * 
    * @return	step
    */
    @Bean
    protected Step step1() {
        return steps.get("step1")
        			.tasklet(new Tasklet() {
						public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
							System.out.println("Simple tasklet execution...");
							// Add logic to perform tasks 
							return RepeatStatus.FINISHED;
						}
        			})
        			.build();
    }
    
    /**
     * A Simple Job definition to executed by JobLauncher with 
     * step defined above. {@link #step1()}
     * 
     * @param step1
     * @return
     * 
     * {@see #JobLauncher}
     */
    @Bean(name = "taskletjob")
    public Job job(@Qualifier("step1") Step step1) {
    	return jobs.get("taskletjob")
    			.incrementer(new RunIdIncrementer())
    			.start(step1)
    			.build();
    }
    
    
}
