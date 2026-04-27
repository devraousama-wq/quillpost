package com.quillpost.reader;

import com.quillpost.content.repository.WorkspaceRepository;
import com.quillpost.search.SearchResultDto;
import com.quillpost.search.SearchService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Controller
public class SearchReaderController {

    private final WorkspaceRepository workspaces;
    private final SearchService searchService;

    public SearchReaderController(WorkspaceRepository workspaces, SearchService searchService) {
        this.workspaces = workspaces;
        this.searchService = searchService;
    }

    @GetMapping("/w/{workspaceSlug}/search")
    public String search(@PathVariable String workspaceSlug, @RequestParam String q, Model model) {
        UUID workspaceId = workspaces.findBySlug(workspaceSlug).orElseThrow().getId();
        List<SearchResultDto> results = searchService.searchResults(workspaceId, q);
        model.addAttribute("workspaceSlug", workspaceSlug);
        model.addAttribute("query", q);
        model.addAttribute("results", results);
        return "reader/search";
    }
}
