package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineCreateRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.LineStationResponse;
import nextstep.subway.line.dto.LineUpdateRequest;
import nextstep.subway.section.application.SectionService;
import nextstep.subway.section.domain.Section;
import nextstep.subway.line.dto.LineSectionCreateRequest;
import nextstep.subway.section.dto.SectionAddRequest;
import nextstep.subway.section.dto.SectionAddResponse;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private LineRepository lineRepository;
    private SectionService sectionService;

    public LineService(LineRepository lineRepository,
                       SectionService sectionService) {
        this.sectionService = sectionService;
        this.lineRepository = lineRepository;
    }

    public LineResponse saveLine(LineCreateRequest request) {
        Line persistLine = lineRepository.save(request.toLine());
        sectionService.saveSection(
                new LineSectionCreateRequest(persistLine,
                        request.getUpStationId(),
                        request.getDownStationId(),
                        request.getDistance(),
                        true));
        return LineResponse.of(persistLine);
    }

    public List<LineResponse> getLines() {
        return lineRepository.findAll()
                .stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public LineStationResponse getOne(Long id) {
        Line line = lineRepository.getWithStations(id);
        return LineStationResponse.of(lineRepository.getOne(id), sectionService.getOrderedSections(line));
    }


    public LineResponse modify(Long id, LineUpdateRequest lineRequest) {
        Line modifyingItem = lineRepository.getOne(id);
        modifyingItem.update(lineRequest.getName(), lineRequest.getColor());
        return LineResponse.of(modifyingItem);
    }

    public void delete(Long id) {
        Line deletingItem = lineRepository.getOne(id);
        sectionService.delete(deletingItem.getSections());
        lineRepository.delete(lineRepository.getOne(id));
    }

    public SectionAddResponse addSection(Long lineId, SectionAddRequest sectionAddRequest) {
        Line addingLine = lineRepository.getOne(lineId);
        SectionAddResponse response = sectionService.addSection(addingLine, sectionAddRequest);
        return response;
    }

}
