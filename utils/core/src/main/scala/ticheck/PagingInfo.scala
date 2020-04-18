package ticheck

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/7/2020
  *
  */
object PagingInfo {

  lazy val defaultPageSize: PageSize = PageSize(20)

  lazy val maxPageSize: PageSize = PageSize(50)

  lazy val defaultPagingInfo: PagingInfo = new PagingInfo(PageNumber(0), defaultPageSize)

  def apply(pageNumber: Option[PageNumber], pageSize: Option[PageSize]): PagingInfo = {
    val correctedPageNumber = pageNumber.map(p => if (p < 1) PageNumber(1) else p).getOrElse(PageNumber(1))
    val correctedPageSize = pageSize
      .map(ps => if (ps < 0) defaultPageSize else if (ps > 50) maxPageSize else PageSize(ps))
      .getOrElse(defaultPageSize)
    new PagingInfo(correctedPageNumber, correctedPageSize)
  }

  def apply(pageNumber: PageNumber, pageSize: PageSize): PagingInfo = new PagingInfo(pageNumber, pageSize)
}
final case class PagingInfo private (pageNumber: PageNumber, pageSize: PageSize) {

  def getOffset: Offset = Offset.spook((pageNumber - 1) * pageSize)

  def getLimit: Limit = Limit.spook((pageNumber - 1) * pageSize + pageSize)

}
