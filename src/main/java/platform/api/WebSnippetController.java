package platform.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import platform.exception.ResourceNotFoundException;
import platform.model.Snippet;
import platform.repository.SnippetRepository;
import platform.service.SnippetService;

import java.util.List;
import java.util.UUID;

@Controller
public class WebSnippetController {

    @Autowired
    private SnippetRepository snippetRepository;

    @Autowired
    SnippetService snippetService;

    @GetMapping("/code/new")
    public String addSnippetWeb() {
        return "create_snippet";
    }

    @GetMapping("/code/{uuid}")
    public String getBySnippetId(@PathVariable UUID uuid, Model model) throws ResourceNotFoundException {
        Snippet snippet = snippetRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (isRestrict(snippet)) {
            snippetRepository.save(snippet);
        } else if (snippet.isRestrictTime() || snippet.isRestrictView()){
            snippetRepository.delete(snippet);
            throw new ResourceNotFoundException("Snippet not found for id: " + uuid);
        }
        model.addAttribute("snippet",  snippet);
        return "snippet";
    }

    @GetMapping("/code/latest")
    public String allSnippet(Model model) {
        List<Snippet> snippets = snippetService.getAllSnippet().getContent();

        model.addAttribute("snippets",  snippets);
        return "latest";
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
