/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason                                                #
 * # Copyright (c) 2015-19, LightJason (info@lightjason.org)                            #
 * # This program is free software: you can redistribute it and/or modify               #
 * # it under the terms of the GNU Lesser General Public License as                     #
 * # published by the Free Software Foundation, either version 3 of the                 #
 * # License, or (at your option) any later version.                                    #
 * #                                                                                    #
 * # This program is distributed in the hope that it will be useful,                    #
 * # but WITHOUT ANY WARRANTY; without even the implied warranty of                     #
 * # MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                      #
 * # GNU Lesser General Public License for more details.                                #
 * #                                                                                    #
 * # You should have received a copy of the GNU Lesser General Public License           #
 * # along with this program. If not, see http://www.gnu.org/licenses/                  #
 * ######################################################################################
 * @endcond
 */

package org.lightjason.agentspeak.action.listsettuple;

import com.codepoetics.protonpack.StreamUtils;
import com.google.common.collect.HashMultimap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.testing.IBaseTest;

import java.text.MessageFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * test collection action
 */
public final class TestCActionCollection extends IBaseTest
{

    /**
     * data provider generator
     * @return data
     */
    public static Stream<Arguments> generate()
    {
        return Stream.of(

            Arguments.of( Stream.of( new Object() ), new int[]{0} ),
            Arguments.of( Stream.of( new ArrayList<>() ), new int[]{0} ),
            Arguments.of( Stream.of( Stream.of( 1, "test" ).collect( Collectors.toList() ) ), new int[]{2} ),
            Arguments.of( Stream.of( new AbstractMap.SimpleEntry<>( "a", 1 ) ), new int[]{0} ),
            Arguments.of( Stream.of( Stream.of( 1, 1, "test" ).collect( Collectors.toSet() ) ), new int[]{2} ),
            Arguments.of( Stream.of( Stream.of( "abcd", "xyz", 12, 12 ).collect( Collectors.toSet() ),
                                     Stream.of( 1, 2, 3, 3, 4, 4 ).collect( Collectors.toList() ) ), new int[]{3, 6} ),
            Arguments.of( Stream.of( StreamUtils.windowed( Stream.of( 1, 2, 3, 4 ), 2 )
                                                .collect( Collectors.toMap( i -> i.get( 0 ), i -> i.get( 1 ) ) ) ), new int[]{0} ),
            Arguments.of( Stream.of( StreamUtils.windowed( Stream.of( 1, 2, 3, 4 ), 2 )
                                                .collect( Collectors.toMap( i -> i.get( 0 ), i -> i.get( 1 ) ) ) ), new int[]{0} )
        );
    }

    /**
     * test size
     *
     * @param p_input input data term
     * @param p_result results
     */
    @ParameterizedTest
    @MethodSource( "generate" )
    public void size( final Stream<Object> p_input, final int[] p_result )
    {
        final List<ITerm> l_return = new ArrayList<>();
        final List<ITerm> l_input = p_input.map( CRawTerm::of ).collect( Collectors.toList() );

        new CSize().execute(
            false, IContext.EMPTYPLAN,
            l_input,
            l_return
        );

        Assertions.assertArrayEquals(
            p_result,
            l_return.stream().map( ITerm::<Number>raw ).mapToInt( Number::intValue ).toArray(),
            MessageFormat.format( "elements {0}", l_input )
        );
    }

    /**
     * test empty list set
     */
    @Test
    public void emptylistset()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CIsEmpty().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( new ArrayList<>(), new HashSet<>(), HashMultimap.create(), new HashMap<>(), Stream.of( "1", 2 ).collect( Collectors.toList() ), new Object() )
                  .map( CRawTerm::of )
                  .collect( Collectors.toList() ),
            l_return
        );

        Assertions.assertEquals( 6, l_return.size() );
        Assertions.assertArrayEquals( Stream.of( true, true, false, false, false, false ).toArray(), l_return.stream().map( ITerm::<Boolean>raw ).toArray() );
    }

    /**
     * test clear
     */
    @Test
    public void clearlistset()
    {
        final List<Integer> l_list = IntStream.range( 0, 10 ).boxed().collect( Collectors.toList() );
        final Set<Integer> l_set = IntStream.range( 10, 20 ).boxed().collect( Collectors.toSet() );

        new CClear().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( l_list, l_set ).map( CRawTerm::of ).collect( Collectors.toList() ),
            Collections.emptyList()
        );

        Assertions.assertTrue( l_list.isEmpty() );
        Assertions.assertTrue( l_set.isEmpty() );
    }

    /**
     * test complement action
     */
    @Test
    public void complement()
    {
        final List<ITerm> l_return = new ArrayList<>();

        Assertions.assertTrue(
            execute(
                new CComplement(),
                false,
                Stream.of(
                    CRawTerm.of( Stream.of( "a", "b", 1, 2 ).collect( Collectors.toList() ) ),
                    CRawTerm.of( Stream.of( "x", "y", 4, "a", 5, 1 ).collect( Collectors.toList() ) )
                ).collect( Collectors.toList() ),
                l_return
            )
        );

        Assertions.assertEquals( 1, l_return.size() );
        Assertions.assertTrue( l_return.get( 0 ).raw() instanceof List<?> );
        Assertions.assertEquals( "b", l_return.get( 0 ).<List<?>>raw().get( 0 ) );
        Assertions.assertEquals( 2, l_return.get( 0 ).<List<?>>raw().get( 1 ) );
    }

    /**
     * test intersect
     */
    @Test
    public void intersect()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CIntersect().execute(
            false, IContext.EMPTYPLAN,
            Stream.of(
                Stream.of( 1, 2 ).collect( Collectors.toList() ),
                Stream.of( 3, 4, 2 ).collect( Collectors.toSet() ),
                Stream.of( 8, 9, 2 ).collect( Collectors.toList() ),
                Stream.of( 1, 2, 3, 5 ).collect( Collectors.toSet() )
            ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assertions.assertEquals( 1, l_return.size() );
        Assertions.assertTrue( l_return.get( 0 ).raw() instanceof List<?> );
        Assertions.assertArrayEquals( Stream.of( 2 ).toArray(), l_return.get( 0 ).<Collection<?>>raw().toArray() );
    }

    /**
     * test symmetric difference
     */
    @Test
    public void symmetricdifference()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CSymmetricDifference().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( 1, 2, 3, 3, 4 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return );

        Assertions.assertEquals( 1, l_return.size() );
        Assertions.assertTrue( l_return.get( 0 ).raw() instanceof List<?> );
        Assertions.assertArrayEquals( Stream.of( 1, 2, 4 ).toArray(), l_return.get( 0 ).<List<?>>raw().toArray() );
    }

    /**
     * test union
     */
    @Test
    public void union()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CUnion().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( 1, 2, 3, 3, 4, Stream.of( "xxx" ).collect( Collectors.toList() ) ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assertions.assertEquals( 1, l_return.size() );
        Assertions.assertTrue( l_return.get( 0 ).raw() instanceof List<?> );
        Assertions.assertArrayEquals( Stream.of( 1, 2, 3, 4, "xxx" ).toArray(), l_return.get( 0 ).<Collection<?>>raw().toArray() );
    }
}
