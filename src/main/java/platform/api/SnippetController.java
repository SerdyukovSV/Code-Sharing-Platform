package platform.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import platform.exception.ResourceNotFoundException;
import platform.model.Snippet;
import platform.repository.SnippetRepository;
import platform.service.SnippetService;

import java.util.*;

@RestController
public class SnippetController {

    @Autowired
    private SnippetRepository snippetRepository;

    @Autowired
    SnippetService snippetService;

    @PostMapping(path = "/api/code/new")
    public String addSnippetJson(@RequestBody Snippet snippet) {
        snippetRepository.save(snippet);
        return "{\"id\" : \"" + snippet.getUuid() + "\"}";
    }

    @GetMapping(path = "/api/code/{uuid}")
    public Snippet getBySnippetId(@PathVariable UUID uuid) throws ResourceNotFoundException {
        Snippet snippet = snippetRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Snippet not found for id: " + uuid));
        if (isRestrict(snippet)) {
            snippetRepository.save(snippet);
        } else if (snippet.isRestrictTime() || snippet.isRestrictView()){
            snippetRepository.delete(snippet);
            throw new ResourceNotFoundException("Snippet not found for id: " + uuid);
        }
        return snippet;
    }

    @GetMapping(path = "/api/code/latest")
    public List<Snippet> allSnippet() {
        return snippetService.getAllSnippet().getContent();
    }

    public boolean isRestrict(Snippet snippet) {
        if (snippet.isRestrictTime() && snippet.isRestrictView()) {
            if (snippet.getTime() > 0 && snippet.getViews() > 0) {
                snippet.setViews(snippet.getViews() - 1);
                return true;
            }
        } else if (snippet.isRestrictView()) {
            if (snippet.getViews() > 0) {
                snippet.setViews(snippet.getViews() - 1);
                return true;
            }
        } else if (snippet.isRestrictTime()) {
            if (snippet.getTime() == 0) {
                return true;
            }
        }
        return false;
    }
}
