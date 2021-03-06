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

import com.google.common.collect.Lists;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.lightjason.agentspeak.action.listsettuple.list.CAdd;
import org.lightjason.agentspeak.action.listsettuple.list.CCreate;
import org.lightjason.agentspeak.action.listsettuple.list.CFlat;
import org.lightjason.agentspeak.action.listsettuple.list.CFlatConcat;
import org.lightjason.agentspeak.action.listsettuple.list.CGet;
import org.lightjason.agentspeak.action.listsettuple.list.CPullEnd;
import org.lightjason.agentspeak.action.listsettuple.list.CPullFront;
import org.lightjason.agentspeak.action.listsettuple.list.CPushEnd;
import org.lightjason.agentspeak.action.listsettuple.list.CPushFront;
import org.lightjason.agentspeak.action.listsettuple.list.CRange;
import org.lightjason.agentspeak.action.listsettuple.list.CRemove;
import org.lightjason.agentspeak.action.listsettuple.list.CReverse;
import org.lightjason.agentspeak.action.listsettuple.list.CSet;
import org.lightjason.agentspeak.action.listsettuple.list.CSubList;
import org.lightjason.agentspeak.action.listsettuple.list.CToSet;
import org.lightjason.agentspeak.action.listsettuple.list.CUnique;
import org.lightjason.agentspeak.action.listsettuple.list.CZip;
import org.lightjason.agentspeak.error.context.CExecutionIllegealArgumentException;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.IExecution;
import org.lightjason.agentspeak.testing.IBaseTest;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * test collection list
 */
public final class TestCActionCollectionList extends IBaseTest
{

    /**
     * test create empty list
     */
    @Test
    public void createempty()
    {
        final List<ITerm> l_return = new ArrayList<>();

        Assertions.assertTrue(
            execute(
                new CCreate(),
                false,
                Collections.emptyList(),
                l_return
            )
        );

        Assertions.assertEquals( 1, l_return.size() );
        Assertions.assertTrue( l_return.get( 0 ).raw() instanceof List<?> );
        Assertions.assertTrue( l_return.get( 0 ).<List<?>>raw().isEmpty() );
    }


    /**
     * test create empty synchronized list
     */
    @Test
    public void createemptysynchronize()
    {
        final List<ITerm> l_return = new ArrayList<>();

        Assertions.assertTrue(
            execute(
                new CCreate(),
                    true,
                    Collections.emptyList(),
                    l_return
            )
        );

        Assertions.assertEquals( 1, l_return.size() );
        Assertions.assertTrue( l_return.get( 0 ).raw() instanceof List<?> );
        Assertions.assertTrue( l_return.get( 0 ).<List<?>>raw().isEmpty() );
        Assertions.assertEquals( Collections.synchronizedList( Collections.emptyList() ).getClass(), l_return.get( 0 ).raw().getClass() );
    }


    /**
     * test create non-empty list
     */
    @Test
    public void create()
    {
        final List<ITerm> l_return = new ArrayList<>();

        Assertions.assertTrue(
            execute(
                new CCreate(),
                    false,
                    Stream.of( "a", 1, "b", true ).map( CRawTerm::of ).collect( Collectors.toList() ),
                    l_return
            )
        );

        Assertions.assertEquals( 1, l_return.size() );
        Assertions.assertTrue( l_return.get( 0 ).raw() instanceof List<?> );
        Assertions.assertEquals( 4, l_return.get( 0 ).<List<?>>raw().size() );

        final List<?> l_list = l_return.get( 0 ).raw();

        Assertions.assertEquals( "a", l_list.get( 0 ) );
        Assertions.assertEquals( 1, l_list.get( 1 ) );
        Assertions.assertEquals( "b", l_list.get( 2 ) );
        Assertions.assertEquals( true, l_list.get( 3 ) );
    }


    /**
     * test get action
     */
    @Test
    public void get()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final List<?> l_list = Stream.of( "a", 1, "b", true, "foobar", 56.78 ).collect( Collectors.toList() );

        Assertions.assertTrue(
            execute(
                new CGet(),
                false,
                Stream.of( CRawTerm.of( l_list ), CRawTerm.of( 1 ), CRawTerm.of( 4 ), CRawTerm.of( 5 ) ).collect( Collectors.toList() ),
                l_return
            )
        );

        Assertions.assertEquals( 3, l_return.size() );
        Assertions.assertEquals( 1, l_return.get( 0 ).<Number>raw() );
        Assertions.assertEquals( "foobar", l_return.get( 1 ).raw() );
        Assertions.assertEquals( 56.78, l_return.get( 2 ).<Number>raw() );
    }

    /**
     * test reverse action
     */
    @Test
    public void reverse()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final List<?> l_list = IntStream.range( 0, 10 ).mapToObj( i -> Math.random() ).collect( Collectors.toList() );

        Assertions.assertTrue(
            execute(
                new CReverse(),
                false,
                Stream.of( CRawTerm.of( l_list ) ).collect( Collectors.toList() ),
                l_return
            )
        );

        Assertions.assertArrayEquals( Lists.reverse( l_list ).toArray(), l_return.stream().map( ITerm::raw ).toArray() );
    }


    /**
     * test remove action
     */
    @Test
    public void remove()
    {
        final Random l_random = new Random();

        final List<?> l_elements = IntStream.range( 0, l_random.nextInt( 100 ) + 1 ).map( i -> l_random.nextInt() ).boxed().collect( Collectors.toList() );
        final List<?> l_list = new ArrayList<>( l_elements );
        final List<Integer> l_index = IntStream.range( 0, l_list.size() / 3 )
                                               .map( i -> l_random.nextInt( l_list.size() ) )
                                               .boxed().distinct().collect( Collectors.toList() );

        final int l_startsize = l_list.size();
        final List<ITerm> l_return = new ArrayList<>();

        Assertions.assertTrue(
            execute(
                new CRemove(),
                false,
                Stream.concat(
                    Stream.of( l_list ),
                    l_index.stream()
                ).map( CRawTerm::of ).collect( Collectors.toList() ),
                l_return
            )
        );

        Assertions.assertEquals( l_list.size() + l_index.size(), l_startsize );
        Assertions.assertTrue(
            l_index.parallelStream()
                   .map( l_elements::get )
                   .allMatch( i -> l_return.parallelStream().map( ITerm::<Number>raw ).anyMatch( j -> j.equals( i ) ) )
        );
    }


    /**
     * test set action
     */
    @Test
    public void set()
    {
        final List<?> l_list1 = Stream.of( "" ).collect( Collectors.toList() );
        final List<?> l_list2 = Stream.of( "abc", 123, true ).collect( Collectors.toList() );

        Assertions.assertTrue(
            execute(
                new CSet(),
                false,
                Stream.of( CRawTerm.of( 0 ), CRawTerm.of( "xxx" ), CRawTerm.of( l_list1 ), CRawTerm.of( l_list2 ) ).collect( Collectors.toList() ),
                Collections.emptyList()
            )
        );

        Assertions.assertEquals( 1, l_list1.size() );
        Assertions.assertEquals( "xxx", l_list1.get( 0 ) );

        Assertions.assertEquals( 3, l_list2.size() );
        Assertions.assertEquals( "xxx", l_list2.get( 0 ) );
    }


    /**
     * test add action
     */
    @Test
    public void add()
    {
        final List<?> l_list = new ArrayList<>();

        Assertions.assertTrue(
            execute(
                new CAdd(),
                false,
                Stream.of( CRawTerm.of( "xyz" ), CRawTerm.of( l_list ) ).collect( Collectors.toList() ),
                Collections.emptyList()
            )
        );

        Assertions.assertEquals( 1, l_list.size() );
        Assertions.assertEquals( "xyz", l_list.get( 0 ) );
    }


    /**
     * test range error
     */
    @Test
    public void rangeerror()
    {
        Assertions.assertThrows( CExecutionIllegealArgumentException.class,
                                () -> new CRange().execute(
                                        false,
                                        IContext.EMPTYPLAN,
                                        Collections.emptyList(),
                                        Collections.emptyList()
                                    )
        );
    }


    /**
     * test range
     */
    @Test
    public void range()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final IExecution l_range = new CRange();

        l_range.execute(
            false, IContext.EMPTYPLAN,
            Stream.of( 0, 5, 7, 9 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        l_range.execute(
            true, IContext.EMPTYPLAN,
            Stream.of( 1, 7 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assertions.assertEquals( l_return.size(), 3 );

        Assertions.assertArrayEquals( IntStream.range( 0, 5 ).boxed().toArray(), l_return.get( 0 ).<List<?>>raw().toArray() );
        Assertions.assertArrayEquals( IntStream.range( 7, 9 ).boxed().toArray(), l_return.get( 1 ).<List<?>>raw().toArray() );

        Assertions.assertArrayEquals( IntStream.range( 1, 7 ).boxed().toArray(), l_return.get( 2 ).<List<?>>raw().toArray() );
        Assertions.assertEquals( Collections.synchronizedList( Collections.emptyList() ).getClass(), l_return.get( 2 ).<List<?>>raw().getClass() );
    }

    /**
     * test sublist error
     */
    @Test
    public void sublisterror()
    {
        Assertions.assertThrows( CExecutionIllegealArgumentException.class,
                                 () -> new CSubList().execute(
                                        false,
                                        IContext.EMPTYPLAN,
                                        Stream.of( new ArrayList<>() ).map( CRawTerm::of ).collect( Collectors.toList() ),
                                        Collections.emptyList()
                                    )
        );
    }

    /**
     * test sublist
     */
    @Test
    public void sublist()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final IExecution l_sublist = new CSubList();

        l_sublist.execute(
            false, IContext.EMPTYPLAN,
            Stream.of( Stream.of( "ax", "bx", "c", 1, 2, 3 ).collect( Collectors.toList() ), 0, 2, 2, 4 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        l_sublist.execute(
            true, IContext.EMPTYPLAN,
            Stream.of( Stream.of( 8, 9, 10 ).collect( Collectors.toList() ), 1, 2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assertions.assertEquals( 3, l_return.size() );

        Assertions.assertArrayEquals( Stream.of( "ax", "bx" ).toArray(), l_return.get( 0 ).<List<?>>raw().toArray() );
        Assertions.assertArrayEquals( Stream.of( "c", 1 ).toArray(), l_return.get( 1 ).<List<?>>raw().toArray() );
        Assertions.assertArrayEquals( Stream.of( 9 ).toArray(), l_return.get( 2 ).<List<?>>raw().toArray() );
    }


    /**
     * test flat action
     */
    @Test
    public void flat()
    {
        final Random l_random = new Random();

        final List<ITerm> l_return = new ArrayList<>();
        final List<?> l_list = IntStream.range( 0, l_random.nextInt( 100 ) + 1 )
                                        .mapToObj( i -> RandomStringUtils.random( l_random.nextInt( 100 ) + 1 ) )
                                        .collect( Collectors.toList() );

        new CFlat().execute(
            false, IContext.EMPTYPLAN,
            l_list.stream().map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assertions.assertEquals( l_list.size(), l_return.size() );
        Assertions.assertArrayEquals( l_list.toArray(), l_return.stream().map( ITerm::raw ).toArray() );
    }


    /**
     * test flatconcat action
     */
    @Test
    public void flatconcat()
    {
        final Random l_random = new Random();

        final List<ITerm> l_return = new ArrayList<>();
        final List<?> l_list = IntStream.range( 0, l_random.nextInt( 100 ) + 1 )
                                        .mapToObj( i -> RandomStringUtils.random( l_random.nextInt( 100 ) + 1 ) )
                                        .collect( Collectors.toList() );

        new CFlatConcat().execute(
            false, IContext.EMPTYPLAN,
            l_list.stream().map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assertions.assertEquals( l_return.size(), 1 );
        Assertions.assertArrayEquals( l_return.get( 0 ).<List<?>>raw().toArray(), l_list.toArray() );
    }


    /**
     * test zip action error
     */
    @Test
    public void ziperror()
    {
        Assertions.assertThrows( CExecutionIllegealArgumentException.class,
                                 () -> new CZip().execute(
                                    false,
                                    IContext.EMPTYPLAN,
                                    Stream.of( "" ).map( CRawTerm::of ).collect( Collectors.toList() ),
                                    Collections.emptyList()
                                )
        );
    }


    /**
     * test zip action
     */
    @Test
    public void zip()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final IExecution l_zip = new CZip();

        l_zip.execute(
            false, IContext.EMPTYPLAN,
            IntStream.range( 0, 6 ).boxed().map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assertions.assertEquals( 1, l_return.size() );
        Assertions.assertEquals( 3, l_return.get( 0 ).<List<?>>raw().size() );

        Assertions.assertEquals( 0, l_return.get( 0 ).<List<AbstractMap.SimpleEntry<?, ?>>>raw().get( 0 ).getKey() );
        Assertions.assertEquals( 3, l_return.get( 0 ).<List<AbstractMap.SimpleEntry<?, ?>>>raw().get( 0 ).getValue() );

        Assertions.assertEquals( 1, l_return.get( 0 ).<List<AbstractMap.SimpleEntry<?, ?>>>raw().get( 1 ).getKey() );
        Assertions.assertEquals( 4, l_return.get( 0 ).<List<AbstractMap.SimpleEntry<?, ?>>>raw().get( 1 ).getValue() );

        Assertions.assertEquals( 2, l_return.get( 0 ).<List<AbstractMap.SimpleEntry<?, ?>>>raw().get( 2 ).getKey() );
        Assertions.assertEquals( 5, l_return.get( 0 ).<List<AbstractMap.SimpleEntry<?, ?>>>raw().get( 2 ).getValue() );

        l_zip.execute(
            true, IContext.EMPTYPLAN,
            Stream.of( 1, 2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assertions.assertEquals( 2, l_return.size() );
        Assertions.assertEquals( Collections.synchronizedList( Collections.emptyList() ).getClass(), l_return.get( 1 ).<List<?>>raw().getClass() );

    }


    /**
     * test unique action
     */
    @Test
    public void unique()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final IExecution l_unique = new CUnique();

        l_unique.execute(
            false, IContext.EMPTYPLAN,
            Stream.of( 1, 1, 3, 4, 5, 5 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assertions.assertEquals( l_return.size(), 1 );
        Assertions.assertEquals( l_return.get( 0 ).<List<?>>raw().size(), 4 );
        Assertions.assertArrayEquals( l_return.get( 0 ).<List<?>>raw().toArray(), Stream.of( 1, 3, 4, 5 ).toArray() );

        l_unique.execute(
            true, IContext.EMPTYPLAN,
            Stream.of( 1 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assertions.assertEquals( 2, l_return.size() );
        Assertions.assertEquals( Collections.synchronizedList( Collections.emptyList() ).getClass(), l_return.get( 1 ).<List<?>>raw().getClass() );
    }

    /**
     * test to-set
     */
    @Test
    public void toset()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CToSet().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( Stream.of( 1, 1, 3, 4, 5, 5 ).collect( Collectors.toList() ) ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assertions.assertEquals( 1, l_return.size() );
        Assertions.assertTrue( l_return.get( 0 ).raw() instanceof Set<?> );
        Assertions.assertArrayEquals( Stream.of( 1, 3, 4, 5 ).toArray(), l_return.get( 0 ).<Collection<?>>raw().toArray() );
    }

    /**
     * test lambda
     */
    @Test
    public void lambda()
    {
        final List<Object> l_data = Stream.of( 1, 12.0, "foobar" ).collect( Collectors.toList() );

        Assertions.assertArrayEquals(
            l_data.toArray(),
            new CLambdaStreaming().apply( l_data ).toArray()
        );
    }

    /**
     * pushs elements to the front / end
     */
    @Test
    public void pushfrontend()
    {
        final List<Object> l_data = Stream.of( "x", "y" ).collect( Collectors.toList() );

        new CPushFront().execute(
            false,
            IContext.EMPTYPLAN,
            Stream.of( l_data, 1, 2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            Collections.emptyList()
        );

        Assertions.assertArrayEquals(
            Stream.of( 2, 1, "x", "y" ).toArray(),
            l_data.toArray()
        );

        new CPushEnd().execute(
            false,
            IContext.EMPTYPLAN,
            Stream.of( l_data, 10, 20 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            Collections.emptyList()
        );

        Assertions.assertArrayEquals(
            Stream.of( 2, 1, "x", "y", 10, 20 ).toArray(),
            l_data.toArray()
        );
    }

    /**
     * pull elements from the front / end
     */
    @Test
    public void pullfrontend()
    {
        final List<Object> l_data = IntStream.range( 5, 15 ).boxed().collect( Collectors.toList() );

        final List<ITerm> l_return = new ArrayList<>();

        new CPullFront().execute(
            false,
            IContext.EMPTYPLAN,
            Stream.of( l_data, 2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assertions.assertArrayEquals( Stream.of( 5, 6 ).toArray(), l_return.stream().map( ITerm::raw ).toArray() );
        Assertions.assertArrayEquals( Stream.of( 7, 8, 9, 10, 11, 12, 13, 14 ).toArray(), l_data.toArray() );

        l_return.clear();

        new CPullEnd().execute(
            false,
            IContext.EMPTYPLAN,
            Stream.of( l_data, 3 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assertions.assertArrayEquals( Stream.of( 14, 13, 12 ).toArray(), l_return.stream().map( ITerm::raw ).toArray() );
        Assertions.assertArrayEquals( Stream.of( 7, 8, 9, 10, 11 ).toArray(), l_data.toArray() );
    }

}
