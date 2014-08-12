package com.ludo.jar2pom.core.file;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;

import com.ludo.jar2pom.core.model.Descriptor;
import com.ludo.jar2pom.core.remote.DescriptorStrategy;

public class FileJarVisitor extends SimpleFileVisitor<Path> {

    public static final PathMatcher JAR_MATCHER = FileSystems.getDefault().getPathMatcher("glob:**/*.jar");

    private final List<Descriptor> descriptors = new ArrayList<>();

    private final DescriptorStrategy descriptorStrategy;

    private String customHost = null;

    public FileJarVisitor(final DescriptorStrategy descriptorStrategy, final String customHost) {
        super();
        Objects.requireNonNull(descriptorStrategy, "Descriptor strategy cannot be null.");
        this.descriptorStrategy = descriptorStrategy;
        this.customHost = customHost;
    }

    public final String getCustomHost() {
        return this.customHost;
    }

    @Override
    public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {

        if (JAR_MATCHER.matches(file)) {
            this.process(file);
        }
        return super.visitFile(file, attrs);
    }

    protected final void process(final Path file) throws IOException {
        final Descriptor descriptor = this.descriptorStrategy.search(file, this.getCustomHost());
        this.addDescriptor(descriptor);
    }

    public final List<Descriptor> getDescriptors() {
        return this.descriptors;
    }

    public final void addDescriptor(final Descriptor descriptor) {
        CollectionUtils.addIgnoreNull(this.descriptors, descriptor);
    }

}
