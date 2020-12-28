package nextstep.subway.line.ui;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineController {
    private final LineService lineService;

    public LineController(final LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity createLine(@RequestBody LineRequest lineRequest) {
        LineResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> getLines() {
        return ResponseEntity.ok(lineService.getLines());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<LineResponse> getLines(@PathVariable Long id) {
        return ResponseEntity.ok(lineService.getOne(id));
    }
    
    @PatchMapping(value = "/{id}")
    public ResponseEntity<LineResponse> modifyLine(
            @PathVariable Long id,
            @RequestBody LineRequest lineRequest) {
        return ResponseEntity.ok(lineService.modify(id, lineRequest));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteLine(@PathVariable Long id){
        lineService.delete(id);
        return ResponseEntity.ok(id);
    }


}
