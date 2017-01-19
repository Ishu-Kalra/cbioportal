package org.cbioportal.service.impl;

import org.cbioportal.model.Gene;
import org.cbioportal.model.meta.BaseMeta;
import org.cbioportal.persistence.GeneRepository;
import org.cbioportal.service.exception.GeneNotFoundException;
import org.cbioportal.service.util.ChromosomeCalculator;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class GeneServiceImplTest extends BaseServiceImplTest {

    @InjectMocks
    private GeneServiceImpl geneService;

    @Mock
    private GeneRepository geneRepository;
    @Mock
    private ChromosomeCalculator chromosomeCalculator;

    @Test
    public void getAllGenes() throws Exception {

        List<Gene> expectedGeneList = new ArrayList<>();
        Gene gene = new Gene();
        expectedGeneList.add(gene);

        Mockito.when(geneRepository.getAllGenes(ALIAS, PROJECTION, PAGE_SIZE, PAGE_NUMBER, SORT, DIRECTION))
                .thenReturn(expectedGeneList);
        Mockito.doAnswer(invocationOnMock -> {
            ((Gene) invocationOnMock.getArguments()[0]).setChromosome("19");
            return null;
        }).when(chromosomeCalculator).setChromosome(gene);

        List<Gene> result = geneService.getAllGenes(ALIAS, PROJECTION, PAGE_SIZE, PAGE_NUMBER, SORT, DIRECTION);

        Assert.assertEquals(expectedGeneList, result);
        Assert.assertEquals("19", result.get(0).getChromosome());
    }

    @Test
    public void getMetaGenes() throws Exception {

        BaseMeta expectedBaseMeta = new BaseMeta();
        Mockito.when(geneRepository.getMetaGenes(ALIAS)).thenReturn(expectedBaseMeta);
        BaseMeta result = geneService.getMetaGenes(ALIAS);

        Assert.assertEquals(expectedBaseMeta, result);
    }

    @Test(expected = GeneNotFoundException.class)
    public void getGeneByEntrezGeneIdNotFound() throws Exception {

        Mockito.when(geneRepository.getGeneByEntrezGeneId(ENTREZ_GENE_ID)).thenReturn(null);

        geneService.getGene(ENTREZ_GENE_ID.toString());
    }

    @Test
    public void getGeneByEntrezGeneId() throws Exception {

        Gene expectedGene = new Gene();
        Mockito.when(geneRepository.getGeneByEntrezGeneId(ENTREZ_GENE_ID)).thenReturn(expectedGene);
        Mockito.doAnswer(invocationOnMock -> {
            ((Gene) invocationOnMock.getArguments()[0]).setChromosome("X");
            return null;
        }).when(chromosomeCalculator).setChromosome(expectedGene);
        
        Gene result = geneService.getGene(ENTREZ_GENE_ID.toString());

        Assert.assertEquals(expectedGene, result);
        Assert.assertEquals("X", result.getChromosome());
    }

    @Test(expected = GeneNotFoundException.class)
    public void getGeneByHugoGeneSymbolNotFound() throws Exception {

        Mockito.when(geneRepository.getGeneByHugoGeneSymbol(HUGO_GENE_SYMBOL)).thenReturn(null);

        geneService.getGene(HUGO_GENE_SYMBOL);
    }

    @Test
    public void getGeneByHugoGeneSymbol() throws Exception {

        Gene expectedGene = new Gene();
        Mockito.when(geneRepository.getGeneByHugoGeneSymbol(HUGO_GENE_SYMBOL)).thenReturn(expectedGene);
        Mockito.doAnswer(invocationOnMock -> {
            ((Gene) invocationOnMock.getArguments()[0]).setChromosome("Y");
            return null;
        }).when(chromosomeCalculator).setChromosome(expectedGene);
        
        Gene result = geneService.getGene(HUGO_GENE_SYMBOL);

        Assert.assertEquals(expectedGene, result);
        Assert.assertEquals("Y", result.getChromosome());
    }

    @Test
    public void getAliasesOfGeneByEntrezGeneId() throws Exception {

        List<String> expectedAliases = new ArrayList<>();
        expectedAliases.add("alias");
        Mockito.when(geneRepository.getAliasesOfGeneByEntrezGeneId(ENTREZ_GENE_ID)).thenReturn(expectedAliases);
        List<String> result = geneService.getAliasesOfGene(ENTREZ_GENE_ID.toString());

        Assert.assertEquals(expectedAliases, result);
    }

    @Test
    public void getAliasesOfGeneByHugoGeneSymbol() throws Exception {

        List<String> expectedAliases = new ArrayList<>();
        expectedAliases.add("alias");
        Mockito.when(geneRepository.getAliasesOfGeneByHugoGeneSymbol(HUGO_GENE_SYMBOL)).thenReturn(expectedAliases);
        List<String> result = geneService.getAliasesOfGene(HUGO_GENE_SYMBOL);

        Assert.assertEquals(expectedAliases, result);
    }

    @Test
    public void fetchGenes() throws Exception {
        
        List<Gene> expectedGeneList = new ArrayList<>();
        Gene gene = new Gene();
        expectedGeneList.add(gene);

        List<String> geneIds = new ArrayList<>();
        geneIds.add(HUGO_GENE_SYMBOL);
        
        Mockito.doAnswer(invocationOnMock -> {
            ((Gene) invocationOnMock.getArguments()[0]).setChromosome("12");
            return null;
        }).when(chromosomeCalculator).setChromosome(gene);

        Mockito.when(geneRepository.fetchGenesByHugoGeneSymbols(Arrays.asList(HUGO_GENE_SYMBOL), PROJECTION))
                .thenReturn(expectedGeneList);

        List<Gene> result = geneService.fetchGenes(geneIds, GENE_ID_TYPE, PROJECTION);

        Assert.assertEquals(1, result.size());
        Assert.assertEquals(gene, result.get(0));
        Assert.assertEquals("12", result.get(0).getChromosome());
    }

    @Test
    public void fetchMetaGenes() throws Exception {

        BaseMeta expectedBaseMeta1 = new BaseMeta();
        expectedBaseMeta1.setTotalCount(1);
        BaseMeta expectedBaseMeta2 = new BaseMeta();
        expectedBaseMeta2.setTotalCount(1);

        List<String> geneIds = new ArrayList<>();
        geneIds.add(HUGO_GENE_SYMBOL);

        Mockito.when(geneRepository.fetchMetaGenesByHugoGeneSymbols(Arrays.asList(HUGO_GENE_SYMBOL)))
                .thenReturn(expectedBaseMeta2);

        BaseMeta result = geneService.fetchMetaGenes(geneIds, GENE_ID_TYPE);

        Assert.assertEquals((Integer) 1, result.getTotalCount());
    }
}