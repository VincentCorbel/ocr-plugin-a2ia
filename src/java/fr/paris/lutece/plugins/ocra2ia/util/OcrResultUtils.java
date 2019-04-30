/*
 * Copyright (c) 2002-2019, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.ocra2ia.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

import fr.paris.lutece.plugins.ocra2ia.business.A2iaOutput;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

/**
 *
 * Utility class to get Ocr results
 *
 */
public final class OcrResultUtils
{

    /**
     * Default private constructor. Do not call
     */
    private OcrResultUtils( )
    {

        throw new AssertionError( );

    }

    /**
     * Get Ocr results in map.
     *
     * @param strDocumentType
     *            Document type
     *
     * @param dispatchA2iaObject
     *            A2ia Jacob wrapper
     * @param variantResultOcrId
     *            id result Ocr A2ia
     * @return Map result of OCR
     */
    public static Map<String, String> getOcrResults( String strDocumentType, Dispatch dispatchA2iaObject, Variant variantResultOcrId )
    {

        if ( strDocumentType.equalsIgnoreCase( AppPropertiesService.getProperty( OcrConstants.PROPERTY_A2IA_DOCUMENT_RIB ) ) )
        {
            return getRIBResult( dispatchA2iaObject, variantResultOcrId );
        } else if ( strDocumentType.equalsIgnoreCase( AppPropertiesService.getProperty( OcrConstants.PROPERTY_A2IA_DOCUMENT_TAX ) ) )
        {
            return null;
        }

        return null;
    }

    /**
     * Get Ocr results for Rib document.
     *
     * @param dispatchA2iaObject
     *            A2ia Jacob wrapper
     * @param variantResultOcrId
     *            id result Ocr A2ia
     * @return Map result of OCR
     */
    private static Map<String, String> getRIBResult( Dispatch dispatchA2iaObject, Variant variantResultOcrId )
    {

        Map<String, String> mapOcrRibResult = new HashMap<>( );

        List<A2iaOutput> listA2iaOutputRib = new ArrayList<>( );
        listA2iaOutputRib.add( new A2iaOutput( AppPropertiesService.getProperty( OcrConstants.PROPERTY_RIB_RESULT ), OcrConstants.OUTPUT_ZONE_RIB, Variant.VariantString ) );
        listA2iaOutputRib.add( new A2iaOutput( AppPropertiesService.getProperty( OcrConstants.PROPERTY_RIB_RESULT_CODE_BANQUE ), OcrConstants.OUTPUT_ZONE_RIB_CODE_BANQUE, Variant.VariantInt ) );
        listA2iaOutputRib.add( new A2iaOutput( AppPropertiesService.getProperty( OcrConstants.PROPERTY_RIB_RESULT_CODE_GUICHET ), OcrConstants.OUTPUT_ZONE_RIB_CODE_GUICHET, Variant.VariantInt ) );
        listA2iaOutputRib.add( new A2iaOutput( AppPropertiesService.getProperty( OcrConstants.PROPERTY_RIB_RESULT_N_COMPTE ), OcrConstants.OUTPUT_ZONE_RIB_N_COMPTE, Variant.VariantInt ) );
        listA2iaOutputRib.add( new A2iaOutput( AppPropertiesService.getProperty( OcrConstants.PROPERTY_RIB_RESULT_CLE ), OcrConstants.OUTPUT_ZONE_RIB_CLE, Variant.VariantInt ) );
        listA2iaOutputRib.add( new A2iaOutput( AppPropertiesService.getProperty( OcrConstants.PROPERTY_RIB_RESULT_IBAN ), OcrConstants.OUTPUT_ZONE_RIB_IBAN, Variant.VariantString ) );
        listA2iaOutputRib.add( new A2iaOutput( AppPropertiesService.getProperty( OcrConstants.PROPERTY_RIB_RESULT_BIC ), OcrConstants.OUTPUT_ZONE_RIB_BIC, Variant.VariantString ) );



        listA2iaOutputRib.forEach( a2iaOutput ->
        {
            getA2iaOutputResult( a2iaOutput, dispatchA2iaObject, variantResultOcrId, mapOcrRibResult );

        } );

        return mapOcrRibResult;
    }

    /**
     * Call a2ia to get property value.
     *
     * @param a2iaOutput
     *            a2iaOutput object
     * @param dispatchA2iaObject
     *            A2ia Jacob wrapper
     * @param variantResultOcrId
     *            id result Ocr A2ia
     * @param mapResult
     *            map result of OCR
     */
    private static void getA2iaOutputResult( A2iaOutput a2iaOutput, Dispatch dispatchA2iaObject, Variant variantResultOcrId, Map<String, String> mapResult )
    {
        Variant variantResult = Dispatch.call( dispatchA2iaObject, "ObjectProperty", variantResultOcrId.getInt( ), a2iaOutput.getOutputZoneName( ) );
        if ( variantResult != null )
        {
            mapResult.put( a2iaOutput.getKey( ), variantResult.changeType( a2iaOutput.getOutputZoneType( ) ).toString( ) );
        }
    }

}
