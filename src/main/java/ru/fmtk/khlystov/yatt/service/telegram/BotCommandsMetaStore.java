package ru.fmtk.khlystov.yatt.service.telegram;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.StandardServletEnvironment;
import ru.fmtk.khlystov.yatt.service.telegram.command.BotCommandMetadata;
import ru.fmtk.khlystov.yatt.service.telegram.command.ServiceCommand;

@Service
@Slf4j
public class BotCommandsMetaStore {

    private final static List<String> NEW_USER_COMMANDS = List.of("/help", "/start");

    private final Map<String, ServiceCommand> commands = new HashMap<>();

    public void registerCommand(@NonNull String identifier, @NonNull ServiceCommand command) {
        commands.put(identifier, command);
    }

    public static ImmutableList<String> getNewUserCommands() {
        return ImmutableList.copyOf(NEW_USER_COMMANDS);
    }

    public ImmutableList<String> getCommands() {
        return ImmutableList.copyOf(commands.keySet());
    }

    private Class<?>[] findAllConfigurationClassesInPackage(String packageName) {
        final List<Class<?>> result = new ArrayList<>();
        final ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(
                true, new StandardServletEnvironment());
        provider.addIncludeFilter(new AnnotationTypeFilter(BotCommandMetadata.class));
        for (BeanDefinition beanDefinition : provider.findCandidateComponents(packageName)) {
            try {
                result.add(Class.forName(beanDefinition.getBeanClassName()));
            } catch (ClassNotFoundException e) {
                log.error(
                        "Could not resolve class object for bean definition", e);
            }
        }
        return result.toArray(new Class<?>[0]);
    }
}
