package net.prismeon.prismeonserver.log

import org.apache.logging.log4j.core.AbstractLifeCycle
import org.apache.logging.log4j.core.Filter
import org.apache.logging.log4j.core.Layout
import org.apache.logging.log4j.core.LogEvent
import org.apache.logging.log4j.core.appender.AbstractAppender
import org.apache.logging.log4j.core.config.plugins.Plugin
import org.apache.logging.log4j.core.config.plugins.PluginAttribute
import org.apache.logging.log4j.core.config.plugins.PluginElement
import org.apache.logging.log4j.core.config.plugins.PluginFactory
import org.apache.logging.log4j.core.layout.PatternLayout
import java.io.Serializable
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.locks.ReadWriteLock
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.collections.HashMap

@Plugin(
    name = "Queue",
    category = "Core",
    elementType = "appender",
    printObject = true
)
class QueueLogAppender(
    var1: String?,
    var2: Filter?,
    var3: Layout<out Serializable?>?,
    var4: Boolean,
    private val queue: BlockingQueue<String>
) : AbstractAppender(var1, var2, var3, var4) {
    override fun append(var1: LogEvent) {
        if (queue.size >= 250) {
            queue.clear()
        }
        queue.add(layout.toSerializable(var1).toString())
    }

    companion object {
        private const val MAX_CAPACITY = 250
        private val QUEUES: HashMap<String, BlockingQueue<String>> = hashMapOf()
        private val QUEUE_LOCK: ReadWriteLock = ReentrantReadWriteLock()

        @PluginFactory
        fun createAppender(
            @PluginAttribute("name") var0: String?,
            @PluginAttribute("ignoreExceptions") var1: String?,
            @PluginElement("Layout") var2: Layout<out Serializable?>?,
            @PluginElement("Filters") var3: Filter?,
            @PluginAttribute("target") var4: String?
        ): QueueLogAppender? {
            var var2 = var2
            var var4 = var4
            val var5 = java.lang.Boolean.parseBoolean(var1)
            return if (var0 == null) {
                AbstractLifeCycle.LOGGER.error("No name provided for QueueLogAppender")
                null
            } else {
                if (var4 == null) {
                    var4 = var0
                }
                QUEUE_LOCK.writeLock().lock()
                var var6: BlockingQueue<String>? = QUEUES[var4]
                if (var6 == null) {
                    var6 = LinkedBlockingQueue<String>()
                    QUEUES[var4] = var6
                }
                QUEUE_LOCK.writeLock().unlock()
                if (var2 == null) {
                    var2 = PatternLayout.createLayout(null, null, null, null, null, true, false, null, null)
                }
                QueueLogAppender(var0, var3, var2, var5, var6)
            }
        }

        fun getNextLogEvent(var0: String?): String? {
            QUEUE_LOCK.readLock().lock()
            val var1: BlockingQueue<*>? = QUEUES[var0]
            QUEUE_LOCK.readLock().unlock()
            if (var1 != null) {
                try {
                    return var1.take() as String
                } catch (ignored: InterruptedException) {
                }
            }
            return null
        }
    }

}