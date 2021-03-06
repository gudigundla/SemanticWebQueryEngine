/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hp.hpl.jena.sparql.resultset;

import java.io.ByteArrayInputStream ;
import java.io.ByteArrayOutputStream ;
import java.util.Arrays ;
import java.util.Collection ;

import org.junit.Test ;
import org.junit.runner.RunWith ;
import org.junit.runners.Parameterized ;
import org.junit.runners.Parameterized.Parameters ;
import org.openjena.atlas.lib.StrUtils ;

import com.hp.hpl.jena.query.ResultSet ;
import com.hp.hpl.jena.query.ResultSetFactory ;
import com.hp.hpl.jena.query.ResultSetFormatter ;
import com.hp.hpl.jena.sparql.sse.Item ;
import com.hp.hpl.jena.sparql.sse.SSE ;
import com.hp.hpl.jena.sparql.sse.builders.BuilderResultSet ;

@RunWith(Parameterized.class)
public class TestResultSetFormat1
{

    static String[] $rs1 = {
        "(resultset (?a ?b ?c)",
        "  (row (?a 1) (?b 2)       )",
        "  (row (?a 1) (?b 4) (?c 3))",
        ")"} ;

    static String[] $rs2 = {
        "(resultset (?a ?b ?c)",
        "  (row (?a 1) (?b 4) (?c 3))",
        "  (row (?a 1) (?b 2)       )",
        ")"} ;

    static String[] $rs3 = {
        "(resultset (?a ?b ?c)", 
        "  (row (?a 1)        (?c 4))",
        "  (row (?a 1) (?b 2) (?c 3))",
        ")"} ;

    
    @Parameters
    public static Collection<Object[]> data()
    {
        return Arrays.asList(new Object[][] { {$rs1}, {$rs2}, {$rs3} } ) ;
    }

    private final String[] $rs ;
    
    public TestResultSetFormat1(String[] rs)
    {
        this.$rs = rs ;
    }
    
    static ResultSet make(String... strings)
    {
        if ( strings.length == 0 )
            throw new IllegalArgumentException() ;
        
        String x = StrUtils.strjoinNL(strings) ;
        Item item = SSE.parse(x) ;
        return BuilderResultSet.build(item) ;
    }
    
    @Test public void resultset_01()           
    {
        ResultSet rs = make($rs) ; 
        ResultSetFormatter.asText(rs) ;
    }
    
    @Test public void resultset_02()           
    {
        ResultSet rs = make($rs) ; 
        ByteArrayOutputStream out = new ByteArrayOutputStream() ;
        ResultSetFormatter.outputAsXML(out, rs) ;
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray()) ;
        ResultSet rs2 = ResultSetFactory.fromXML(in) ;
    }

    @Test public void resultset_03()           
    {
        ResultSet rs = make($rs) ; 
        ByteArrayOutputStream out = new ByteArrayOutputStream() ;
        ResultSetFormatter.outputAsJSON(out, rs) ;
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray()) ;
        ResultSet rs2 = ResultSetFactory.fromJSON(in) ;
    }
    
    @Test public void resultset_04()           
    {
        ResultSet rs = make($rs) ; 
        ByteArrayOutputStream out = new ByteArrayOutputStream() ;
        ResultSetFormatter.outputAsTSV(out, rs) ;
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray()) ;
        ResultSet rs2 = ResultSetFactory.fromTSV(in) ;
    }

    @Test public void resultset_05()           
    {
        ResultSet rs = make($rs) ; 
        ByteArrayOutputStream out = new ByteArrayOutputStream() ;
        ResultSetFormatter.outputAsCSV(out, rs) ;
    }
}
