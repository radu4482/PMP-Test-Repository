package Lab12

import com.cra.figaro.language._
import com.cra.figaro.algorithm.sampling._
import com.cra.figaro.library.compound.{If}
import com.cra.figaro.algorithm.factored.VariableElimination
import com.cra.figaro.library.atomic.continuous.Normal
import scalax.chart.api._
object Ex1 {
	def main(args: Array[String]) {
		val x0 = Apply(Normal(0.75, 0.3), (d: Double) => d.max(0).min(1))
        val y0 = Apply(Normal(0.4, 0.2), (d: Double) => d.max(0).min(1))
		val x = Flip(x0)
		val y = Flip(y0)
		val z = If(x === y, Flip(0.8), Flip(0.2))
		z.observe(false)
		val veAnswer = VariableElimination.probability(y, true)
		
		val series = new XYSeries("Importance sampling")		
		val chart = XYLineChart(series)
		chart.show()
		for { i <- 1000 to 10000 by 1000 } {
			var totalSquaredError = 0.0
			for { j <- 1 to 100 } {
				val imp = Importance(i, y)
				imp.start()
				val impAnswer = imp.probability(y, true)
				val diff = veAnswer - impAnswer
				totalSquaredError += diff * diff
			}
			val rmse = math.sqrt(totalSquaredError / 100)
			println(i + " samples: RMSE = " + rmse)
			
			series.add(i/1000,rmse)
		}
	}
}

/*Putem observa ca , doar cu 100 de rulari nu e indeajuns pentru a obtine
un graf cat decat precis pentru estimarea erorilor dar putem remarca totusi
faptul ca eroarea scade iar rata la care scade incetineste.*/