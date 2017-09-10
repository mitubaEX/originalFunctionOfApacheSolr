apache solrで使用する独自ファンクションを置いておく置き場

# 使用方法

- Apache Solrで作成したコア直下にlibディレクトリを作成する

```
mkdir $SOLR/server/solr/<coreName>/lib
```

- gradleでbuildしたjarファイルをlib直下に配置する

```
cp build/libs/each-edit-0.0.1-SNAPSHOT.jar $SOLR/server/solr/<coreName>/lib/.
```

- solrconfigに追記する

```
# 下に追加する
<lib path="../lib/each-edit.jar" />
<valueSourceParser name="eachedit"
               class="com.github.mituba.EachEditParser" />
<valueSourceParser name="eachjw"
               class="com.github.mituba.EachJWParser" />
```

後は再起動してsortなりflなりのパラメータにeachedit("a", "b")を使用してクエリを投げる.


# Dependencies

- tdebatty/java-string-similarity
    - https://github.com/tdebatty/java-string-similarity


