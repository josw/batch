package com.josw.batch.jobs

import com.josw.batch.constant.Constant.TEST_JOB
import com.josw.repository.UserRepository
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@ConditionalOnProperty(value = ["job.name"], havingValue = TEST_JOB)
@Configuration
class TestJobConfig(
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory,
    private val userRepository: UserRepository
) {

    @Bean
    fun job(): Job {
        return jobBuilderFactory[TEST_JOB]
            .incrementer(RunIdIncrementer())
            .start(step())
            .build()
    }

    @Bean
    fun step() : Step {
        return stepBuilderFactory["stepname"]
            .tasklet { _,_ ->
                println ("task1")
                RepeatStatus.FINISHED
            }
            .build()
    }
}