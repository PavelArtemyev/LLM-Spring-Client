package ru.llm_spring_client.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import ru.llm_spring_client.repository.DocumentRepository;
import ru.llm_spring_client.repository.entity.LoadedDocument;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentLoaderService implements CommandLineRunner {

    private final DocumentRepository documentRepository;

    private final ResourcePatternResolver resolver;

    private final VectorStore vectorStore;

    @SneakyThrows
    public void loadDocuments() {
        Arrays.stream(resolver.getResources("classpath:/knowledgebase/**/*.txt"))
                .map(resource -> Pair.of(resource, calcContentHash(resource)))
                .filter(pair -> !documentRepository.existsByFilenameAndContentHash(pair.getFirst().getFilename(), pair.getSecond()))
                .forEach(pair -> {
                    Resource resource = pair.getFirst();
                    List<Document> documents = new TextReader(resource).get();
                    TokenTextSplitter textSplitter = TokenTextSplitter.builder().withChunkSize(500).build();
                    List<Document> chunks = textSplitter.apply(documents);
                    vectorStore.accept(chunks);

                    LoadedDocument loadedDocument = LoadedDocument.builder()
                            .documentType("txt")
                            .chunkCount(chunks.size())
                            .filename(resource.getFilename())
                            .contentHash(pair.getSecond())
                            .build();

                    documentRepository.save(loadedDocument);
                });
    }

    @SneakyThrows
    private String calcContentHash(Resource resource) {
        return DigestUtils.md5DigestAsHex(resource.getInputStream());
    }

    @Override
    public void run(String... args) throws Exception {
        loadDocuments();
    }
}
