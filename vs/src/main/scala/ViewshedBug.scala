package com.example.vs

import geotrellis.proj4._
import geotrellis.raster._
import geotrellis.raster.io.geotiff._
import geotrellis.raster.io.geotiff.writer.GeoTiffWriter
import geotrellis.raster.resample.NearestNeighbor
import geotrellis.spark._
import geotrellis.spark.io._
import geotrellis.spark.io.hadoop._
import geotrellis.spark.tiling._
import geotrellis.spark.viewshed._
import geotrellis.vector._

import org.apache.log4j.Logger
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.storage.StorageLevel

import scala.collection.JavaConverters._


object ViewshedBug {

  val logger = Logger.getLogger(ViewshedBug.getClass)

  def main(args: Array[String]) : Unit = {

    // Establish Spark Context
    val sparkConf = (new SparkConf())
      .setAppName("ViewshedBug")
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .set("spark.kryo.registrator", "geotrellis.spark.io.kryo.KryoRegistrator")
      .set("spark.kryo.unsafe", "true")
      .set("spark.rdd.compress", "true")
      .set("spark.ui.enabled", "false")
    val sparkContext = new SparkContext(sparkConf)
    implicit val sc = sparkContext

    // Read data into tile layer
    val targetCRS = CRS.fromEpsgCode(26911)
    val inputRdd: RDD[(ProjectedExtent, Tile)] = HadoopGeoTiffRDD.spatial("/tmp/elev_with_buildings2_clipped2.tif")
    val (_, md) = TileLayerMetadata.fromRDD(inputRdd, FloatingLayoutScheme(512))
    val tiled = ContextRDD(inputRdd.tileToLayout(md.cellType, md.layout, NearestNeighbor), md)

    // Perform viewshed
    val point27 = Viewpoint(x = 370815.36, y = 3734806.85, viewHeight = 1.8, angle = 0, fieldOfView = 360, altitude = -1.0/0)
    val layerVs = tiled.viewshed(Seq(point27), maxDistance=3218.69, curvature=true)

    // Save result
    val raster = layerVs.stitch
    val tiff = GeoTiff(raster, md.crs)
    GeoTiffWriter.write(tiff, "/tmp/moop.tif", optimizedOrder = true)

    // Disestablish Spark Context
    sparkContext.stop

    println(args.toList)
  }
}
