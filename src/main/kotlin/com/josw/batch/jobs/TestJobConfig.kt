package com.josw.batch.jobs

import com.josw.batch.constant.Constant.TEST_JOB
import com.josw.entity.User
import com.josw.repository.UserRepository
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.data.RepositoryItemReader
import org.springframework.batch.item.data.RepositoryItemWriter
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.Sort.Direction


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
            .next(step2())
            .build()
    }

    @Bean
    fun step() : Step {
        return stepBuilderFactory["step1"]
            .tasklet { _,_ ->
                println ("task1")
                RepeatStatus.FINISHED
            }
            .build()
    }

    @Bean
    fun step2() : Step {
        return stepBuilderFactory["step2"]
            .chunk<User, User>(10)
            .reader(reader())
            .processor(processor())
            .writer(writer())
            .build()
    }

    private fun reader(): RepositoryItemReader<out User> {
        return RepositoryItemReader<User>().apply {
            setRepository(userRepository)
            setMethodName("findAllByAge")
            setArguments(
                listOf(30)
            )
            setPageSize(10)
            setSort(mapOf("id" to Direction.ASC))
        }
    }

    private fun processor(): ItemProcessor<User, User> {
        return ItemProcessor<User, User> { item ->
            item.apply {
                age += 1
            }
        }
    }

    private fun writer(): ItemWriter<in User> {
        return RepositoryItemWriter<User>().apply {
            setRepository(userRepository)
            setMethodName("save")
        }
    }
}